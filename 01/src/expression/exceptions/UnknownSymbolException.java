package expression.exceptions;

public class UnknownSymbolException extends ParsingException {
    public UnknownSymbolException(String s, int ind) {
        super("Unknown symbol at position: " + ind + '\n' + s + '\n' + getMessage(ind, 1));
    }
}
