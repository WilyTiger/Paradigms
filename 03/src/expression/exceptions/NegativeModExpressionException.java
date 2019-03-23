package expression.exceptions;

public class NegativeModExpressionException extends EvaluatingException {
    public NegativeModExpressionException() {
        super("Negative mod expression");
    }
}
