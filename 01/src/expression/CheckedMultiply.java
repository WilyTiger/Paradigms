package expression;

import expression.exceptions.OverflowException;

public class CheckedMultiply extends AbstractBinaryOperation {
    public CheckedMultiply(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void check(int left, int right) throws OverflowException {
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

    protected int doOperation(int left, int right) throws OverflowException {
        check(left, right);
        return left * right;
    }
}
