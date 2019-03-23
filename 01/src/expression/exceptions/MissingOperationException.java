package expression.exceptions;

public class MissingOperationException extends ParsingException {
    public MissingOperationException(String s, int ind) {
        super("Missing operation before position: " + ind + '\n' + s + '\n' + getMessage(ind, 1));
    }
}
