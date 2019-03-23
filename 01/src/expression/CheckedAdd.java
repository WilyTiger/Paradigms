package expression;

import expression.exceptions.OverflowException;

public class CheckedAdd extends AbstractBinaryOperation {
    public CheckedAdd(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void check(int left, int right) throws OverflowException {
        if (right > 0 && left > Integer.MAX_VALUE - right) {
            throw new OverflowException();
        }
        if (right < 0 && left < Integer.MIN_VALUE - right) {
            throw new OverflowException();
        }
    }

    protected int doOperation(int left, int right) throws OverflowException {
        check(left, right);
        return left + right;
    }

    protected double doOperation(double left, double right) {
        return left + right;
    }
}
