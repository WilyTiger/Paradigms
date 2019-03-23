package expression.exceptions;

public class MissingCloseBracket extends ParsingException {
    public MissingCloseBracket (String s, final int ind){
        super("Missing closing bracket for opening one at: " + ind + "\n" + s + "\n" + getMessage(ind, 1));
    }
}
