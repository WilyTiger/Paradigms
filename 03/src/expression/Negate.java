package expression;

import expression.exceptions.OverflowException;
import expression.operation.Operation;

public class Negate<T> extends AbstractUnaryOperation<T> {
    public Negate(TripleExpression<T> exp, Operation<T> operation) {
        super(exp, operation);
    }

    protected T doOperation(T operand) throws OverflowException {
        return operation.negate(operand);
    }
}
