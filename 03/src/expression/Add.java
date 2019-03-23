package expression;

import expression.exceptions.OverflowException;
import expression.operation.Operation;

public class Add<T> extends AbstractBinaryOperation<T> {
    public Add(TripleExpression<T> left, TripleExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    protected T doOperation(T left, T right) throws OverflowException {
        return operation.add(left, right);
    }
}
