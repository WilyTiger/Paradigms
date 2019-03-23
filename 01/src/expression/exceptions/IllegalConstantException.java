package expression.exceptions;

public class IllegalConstantException extends ParsingException {
    public IllegalConstantException(String constant, String s, int ind) {
        super("Incorrect constant " + constant + " for int at position: " + ind + '\n' + s + '\n' + getMessage(ind, constant.length()));
    }
}
