package expression;

import expression.exceptions.EvaluatingException;
import expression.operation.Operation;

public abstract class AbstractBinaryOperation<T> implements TripleExpression<T> {
    private TripleExpression<T> left, right;
    protected Operation<T> operation;

    protected AbstractBinaryOperation(TripleExpression<T> nLeft, TripleExpression<T> nRight, Operation<T> nOp) {
        left = nLeft;
        right = nRight;
        operation = nOp;
    }

    abstract protected T doOperation(T left, T right) throws EvaluatingException;

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return doOperation(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

}
