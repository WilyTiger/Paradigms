package expression.exceptions;

public class UnknownIdentifierException extends ParsingException {
    public UnknownIdentifierException(String identifier, String s, int ind) {
        super("Unknown identifier " + identifier + " at position: " + ind + '\n' + s + '\n' + getMessage(ind, identifier.length()));
    }
}
