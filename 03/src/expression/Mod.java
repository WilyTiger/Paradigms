package expression;

import expression.exceptions.EvaluatingException;
import expression.operation.Operation;

public class Mod<T> extends AbstractBinaryOperation<T> {
    public Mod(TripleExpression<T> left, TripleExpression<T> right, Operation<T> operation) {
        super(left, right, operation);
    }

    protected T doOperation(T left, T right) throws EvaluatingException {
        return operation.mod(left, right);
    }
}
