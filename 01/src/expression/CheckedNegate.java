package expression;

import expression.exceptions.OverflowException;

public class CheckedNegate extends AbstractUnaryOperation {
    public CheckedNegate(TripleExpression exp) {
        super(exp);
    }

    private void check(int operand) throws OverflowException {
        if (operand == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }
    protected int doOperation(int operand) throws OverflowException {
        check(operand);
        return -operand;
    }
}
