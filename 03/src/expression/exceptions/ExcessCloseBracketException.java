package expression.exceptions;

public class ExcessCloseBracketException extends ParsingException {
    public ExcessCloseBracketException(String s, int ind) {
        super("Excess close bracket at position: " + ind + '\n' + s + '\n' + getMessage(ind, 1));
    }
}
