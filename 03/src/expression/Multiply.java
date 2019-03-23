package expression;

import expression.exceptions.OverflowException;
import expression.operation.Operation;

public class Multiply<T> extends AbstractBinaryOperation<T> {
    public Multiply(TripleExpression<T> left, TripleExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    protected T doOperation(T left, T right) throws OverflowException {
        return operation.mul(left, right);
    }
}
