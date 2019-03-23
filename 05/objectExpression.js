function Const(value) {
    this.value = value;
}

const zero = new Const(0);
const one = new Const(1);
const two = new Const(2);


Const.prototype.evaluate = function () {
    return this.value;
};

Const.prototype.toString = function () {
    return this.value.toString();
};

Const.prototype.diff = function () {
    return zero;
};

Const.prototype.simplify = function() {
  return this;
};

const VARIABLES = {
    "x" : 0,
    "y" : 1,
    "z" : 2
};

function Variable(name) {
    this.name = name;
}

Variable.prototype.evaluate = function () {
    return arguments[VARIABLES[this.name]];
};

Variable.prototype.toString = function () {
    return this.name.toString();
};

Variable.prototype.diff = function (name) {
    if (this.name === name) {
        return one;
    } else {
        return zero;
    }
};

Variable.prototype.simplify = function() {
    return this;
};

function Operation() {
    this.operands = Array.from(arguments);
    this.getOperands = function() {
        return this.operands;
    }
}

Operation.prototype.evaluate = function (...args) {
    let result = [];
    this.getOperands().map(function (operand) {
        result.push(operand.evaluate(...args));
    });
    return this.doOperation(...result);
};

Operation.prototype.diff = function (name) {
  let args = [];
  this.getOperands().map(function (operand) {
      args.push(operand);
      args.push(operand.diff(name));
  });
  return this.doDiff(...args);
};

Operation.prototype.toString = function () {
    let res = "";
    this.getOperands().map(function (operand) {
        res += operand.toString() + ' ';
    });
    res += this.operationStr;
    return res;
};

Operation.prototype.simplify = function () {
    let operandSimplify = [];
    let flag = true;
    this.getOperands().map(function (operand) {
        let simple = operand.simplify();
        if (!(simple instanceof Const)) {
            flag = false;
        }
        operandSimplify.push(operand.simplify());
    });
    let res = new this.constructor(...operandSimplify);
    if (flag) {
        return new Const(res.evaluate());
    }
    if (this.doSimplify !== undefined) {
        return this.doSimplify(...operandSimplify);
    } else {
        return res;
    }
};

function operationFactory(operationStr, doOperation, doDiff, doSimplify) {
    function Constructor() {
        Operation.apply(this, arguments);
        return this;
    }
    Constructor.prototype = Object.create(Operation.prototype);
    Constructor.prototype.operationStr = operationStr;
    Constructor.prototype.constructor = Constructor;
    Constructor.prototype.doOperation = doOperation;
    Constructor.prototype.doDiff = doDiff;
    Constructor.prototype.doSimplify = doSimplify;
    return Constructor;
}

function checkZero(operand) {
    if (operand instanceof Const) {
        return operand.value === 0;
    }
    return false;
}

function checkOne(operand) {
    if (operand instanceof Const) {
        return operand.value === 1;
    }
    return false;
}

const Add = operationFactory('+',
    (x, y) => x + y,
    (x, dx, y, dy) => new Add(dx, dy),
    (x, y) => {
        if (checkZero(x)) {
            return y;
        }
        if (checkZero(y)) {
            return x;
        }
        return new Add(x, y);
    });

const Subtract = operationFactory('-',
    (x, y) => x - y,
    (x, dx, y, dy) => new Subtract(dx, dy),
    (x, y) => {
        if (checkZero(y)) {
            return x;
        }
        return new Subtract(x, y);
    });

const Multiply = operationFactory('*',
    (x, y) => x * y,
    (x, dx, y, dy) => new Add(new Multiply(dx, y), new Multiply(dy, x)),
    (x, y) => {
        if (checkZero(x) || checkZero(y)) {
            return zero;
        }
        if (checkOne(x)) {
            return y;
        }
        if (checkOne(y)) {
            return x;
        }
        return new Multiply(x, y);
    });

const Divide = operationFactory('/',
    (x, y) => x / y,
    (x, dx, y, dy) => new Divide(new Subtract(new Multiply(dx, y), new Multiply(dy, x)), new Multiply(y, y)),
    (x, y) => {
        if (checkZero(x)) {
            return zero;
        }
        if (checkOne(y)) {
            return x;
        }
        return new Divide(x, y);
    });

const ArcTan = operationFactory('atan',
    (x) => Math.atan(x),
    (x, dx) => new Divide(dx, new Add(one, new Multiply(x, x))),
    (x) => {
        if (x instanceof Const) {
            return new Const(Math.atan(x));
        }
        return new ArcTan(x);
    });

const ArcTan2 = operationFactory('atan2',
    (x, y) => Math.atan2(x, y),
    (x, dx, y, dy) => new Divide(new Subtract(new Multiply(dx, y), new Multiply(dy, x)), new Add(new Multiply(y, y), new Multiply(x, x))),
    (x, y) => {
        if (x instanceof Const && y instanceof Const) {
            return new Const(Math.atan2(x, y));
        }
        return new ArcTan2(x, y);
    });

const Negate = operationFactory('negate', (x) => -x, (x, dx) => new Negate(dx));


const CONSTANT = {
    "one" : 1,
    "two" : 2
};

function parse(expr) {
    const OPERATION = {
        "+" : Add,
        "-" : Subtract,
        "*" : Multiply,
        "/" : Divide,
        "negate": Negate,
        "atan": ArcTan,
        "atan2": ArcTan2
    };

    const CNT_ARGUMENTS = {
        "+" : 2,
        "-" : 2,
        "*" : 2,
        "/" : 2,
        "negate": 1,
        "atan": 1,
        "atan2": 2
    };

    const tokens = expr.split(" ").filter(f => f.length > 0);
    let stack = [];
    tokens.map(function (token) {
        if (token in VARIABLES) {
            stack.push(new Variable(token));
        } else if (token in OPERATION) {
            let arguments = stack.slice(stack.length - CNT_ARGUMENTS[token], stack.length);
            stack.splice(stack.length - CNT_ARGUMENTS[token], CNT_ARGUMENTS[token]);
            stack.push(new OPERATION[token](...arguments));
        } else if (token in CONSTANT) {
            stack.push(new Const(CONSTANT[token]));
        } else {
            stack.push(new Const(Number(token)));
        }
    });

    return stack.pop();
}
