package expression.exceptions;

public class DBZException extends EvaluatingException {
    public DBZException() {
        super("division by zero");
    }
}
