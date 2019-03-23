package expression;

import expression.exceptions.DBZException;
import expression.exceptions.EvaluatingException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends AbstractBinaryOperation {
    public CheckedDivide(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    private void check(int left, int right) throws EvaluatingException {
        if (right == 0) {
            throw new DBZException();
        }
        if (left == Integer.MIN_VALUE && right == -1)
            throw new OverflowException();
    }

    protected int doOperation(int left, int right) throws EvaluatingException {
        check(left, right);
        return left / right;
    }

}
