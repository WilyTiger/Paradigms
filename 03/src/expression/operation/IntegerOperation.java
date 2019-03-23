package expression.operation;

import expression.exceptions.*;

public class IntegerOperation implements Operation<Integer> {
    private boolean checked;

    public IntegerOperation(boolean f) {
        checked = f;
    }

    public Integer getConst(String number) throws IncorrectConstantException {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstantException();
        }
    }

    public Integer add(Integer left, Integer right) throws OverflowException {
        if (checked) {
            checkAdd(left, right);
        }
        return left + right;
    }

    private void checkAdd(Integer left, Integer right) throws OverflowException {
        if (right > 0 && left > Integer.MAX_VALUE - right) {
            throw new OverflowException();
        }
        if (right < 0 && left < Integer.MIN_VALUE - right) {
            throw new OverflowException();
        }
    }

    public Integer sub(Integer left, Integer right) throws OverflowException {
        if (checked) {
            checkSub(left, right);
        }
        return left - right;
    }

    private void checkSub(Integer left, Integer right) throws OverflowException {
        if (right > 0 && left < Integer.MIN_VALUE + right) {
            throw new OverflowException();
        }
        if (right < 0 && left > Integer.MAX_VALUE + right) {
            throw new OverflowException();
        }
    }

    public Integer mul(Integer left, Integer right) throws OverflowException {
        if (checked) {
            checkMul(left, right);
        }
        return left * right;
    }

    private void checkMul(Integer left, Integer right) throws OverflowException {
        if (left > 0 && right > 0 && Integer.MAX_VALUE / left < right) {
            throw new OverflowException();
        }
        if (left > 0 && right < 0 && Integer.MIN_VALUE / left > right) {
            throw new OverflowException();
        }
        if (left < 0 && right > 0 && Integer.MIN_VALUE / right > left) {
            throw new OverflowException();
        }
        if (left < 0 && right < 0 && Integer.MAX_VALUE / left > right) {
            throw new OverflowException();
        }
    }

    public Integer div(Integer left, Integer right) throws EvaluatingException {
        checkDiv(left, right);
        return left / right;
    }

    public Integer mod(Integer left, Integer right) throws EvaluatingException {
        checkDiv(left, right);
        return left % right;
    }

    private void checkDiv(Integer left, Integer right) throws EvaluatingException {
        if (right == 0) {
            throw new DBZException();
        }
        if (left == Integer.MIN_VALUE && right == -1)
            throw new OverflowException();
    }

    public Integer negate(Integer operand) throws OverflowException {
        if (checked) {
            checkNegate(operand);
        }
        return -operand;
    }

    public Integer abs(Integer x) throws OverflowException {
        if (checked) {
            checkAbs(x);
        }
        return Math.abs(x);
    }

    private void checkAbs(Integer x) throws OverflowException {
        if (x.equals(Integer.MIN_VALUE)) {
            throw new OverflowException();
        }
    }

    private void checkNegate(Integer operand) throws OverflowException {
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }
}
