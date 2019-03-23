package expression;

import expression.exceptions.EvaluatingException;

public abstract class AbstractUnaryOperation implements TripleExpression {
    private TripleExpression operand;

    protected AbstractUnaryOperation(TripleExpression x) {
        operand = x;
    }

    protected abstract int doOperation(int x) throws EvaluatingException;

    public int evaluate(int x, int y, int z) throws EvaluatingException {
        return doOperation(operand.evaluate(x, y, z));
    }
}
