package expression;

import expression.exceptions.EvaluatingException;
import expression.operation.Operation;

public class Abs<T> extends AbstractUnaryOperation<T> {
    public Abs(TripleExpression<T> operand, Operation<T> operation) {
        super(operand, operation);
    }

    protected T doOperation(T x) throws EvaluatingException {
        return operation.abs(x);
    }


}
