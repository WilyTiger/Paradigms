package expression;

import expression.exceptions.EvaluatingException;

public abstract class AbstractBinaryOperation implements TripleExpression {
    private TripleExpression left, right;

    protected AbstractBinaryOperation(TripleExpression nLeft, TripleExpression nRight) {
        left = nLeft;
        right = nRight;
    }

    abstract protected int doOperation(int left, int right) throws EvaluatingException;

    public int evaluate(int x, int y, int z) throws EvaluatingException {
        return doOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
