const VARIABLES = {
    "x": 0,
    "y": 1,
    "z": 2
};

const CONSTANT = {
    "pi" : Math.PI,
    "e" : Math.E,
    "one" : 1,
    "two" : 2
};

const cnst = num => () => {
    return num;
};

const variable = name => (...args) => args[VARIABLES[name]];

const operation = f => (...args) => (...variables) => {
    let result = [];
    args.map(function (operand) {
        result.push(operand(...variables));
    });
    return f(...result);
};

const add = operation((f, g) => f + g);

const multiply = operation((f, g) => f * g);

const divide = operation((f, g) => f / g);

const subtract = operation((f, g) => f - g);

const negate = operation((f) => -f);

const avg5 = operation((...args) => {
   let sum = 0;
   for (let i = 0; i < args.length; i++) {
        sum += args[i];
   }
   return sum / args.length;
});

const med3 = operation((x, y, z) => {
    let operands = [x, y, z];
    operands.sort((a, b) => a - b);
    return operands[1];
});

const abs = operation((x) => Math.abs(x));
const iff = operation((x, y, z) => ((x >= 0) ? y : z));

/*let expres = add(subtract(multiply(variable("x"), variable("x")), multiply(cnst(2), variable("x"))), cnst(1));
for (let i = 0; i <= 10; i++) {
    console.log(expr(i));
}*/

const one = cnst(1);
const two = cnst(2);
const pi = cnst(Math.PI);
const e = cnst(Math.E);

function parse(expr) {
    const OPERATION = {
        "+" : add,
        "-" : subtract,
        "*" : multiply,
        "/" : divide,
        "negate": negate,
        "abs": abs,
        "iff": iff,
        "avg5" : avg5,
        "med3" : med3
    };

    const CNT_ARGUMENTS = {
        "+" : 2,
        "-" : 2,
        "*" : 2,
        "/" : 2,
        "negate": 1,
        "avg5" : 5,
        "med3" : 3,
        "abs" : 1,
        "iff" : 3
    };

    const tokens = expr.split(" ").filter(f => f.length > 0);
    let stack = [];
    tokens.map(function (token) {
        if (token in VARIABLES) {
            stack.push(variable(token));
        } else if (token in OPERATION) {
            let arguments = stack.slice(stack.length - CNT_ARGUMENTS[token], stack.length);
            stack.splice(stack.length - CNT_ARGUMENTS[token], CNT_ARGUMENTS[token]);
            stack.push(OPERATION[token](...arguments));
        } else if (token in CONSTANT) {
            stack.push(cnst(CONSTANT[token]));
        } else {
            stack.push(cnst(Number(token)));
        }
    });

    return stack.pop();
}