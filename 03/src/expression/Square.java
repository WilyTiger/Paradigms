package expression;

import expression.exceptions.EvaluatingException;
import expression.operation.Operation;

public class Square<T> extends AbstractUnaryOperation<T> {
    public Square(TripleExpression<T> operand, Operation<T> operation) {
        super(operand, operation);
    }
    protected T doOperation(T x) throws EvaluatingException {
        return operation.square(x);
    }
}
