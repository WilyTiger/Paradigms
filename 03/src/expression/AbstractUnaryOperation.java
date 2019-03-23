package expression;

import expression.exceptions.EvaluatingException;
import expression.operation.Operation;

public abstract class AbstractUnaryOperation<T> implements TripleExpression<T> {
    private TripleExpression<T> operand;
    protected Operation<T> operation;
    protected AbstractUnaryOperation(TripleExpression<T> x, Operation<T> operation) {
        operand = x;
        this.operation = operation;
    }

    protected abstract T doOperation(T x) throws EvaluatingException;

    public T evaluate(T x, T y, T z) throws EvaluatingException {
        return doOperation(operand.evaluate(x, y, z));
    }
}
