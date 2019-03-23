package expression.exceptions;

public class IncorrectConstantException extends EvaluatingException {
    public IncorrectConstantException() {
        super("Bad const");
    }
}
