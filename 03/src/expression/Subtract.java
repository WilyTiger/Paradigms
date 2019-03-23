package expression;

import expression.exceptions.OverflowException;
import expression.operation.Operation;

public class Subtract<T> extends AbstractBinaryOperation<T> {
    public Subtract(TripleExpression<T> left, TripleExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    protected T doOperation(T left, T right) throws OverflowException {
        return operation.sub(left, right);
    }

}
