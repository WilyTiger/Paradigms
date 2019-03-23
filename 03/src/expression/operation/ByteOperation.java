package expression.operation;

import expression.exceptions.*;

public class ByteOperation implements Operation<Byte> {
    public Byte getConst(String number) {
        return (byte) Integer.parseInt(number);
    }

    public Byte add(Byte left, Byte right) {
        return (byte) (left + right);
    }

    public Byte sub(Byte left, Byte right) {
        return (byte) (left - right);
    }

    public Byte mul(Byte left, Byte right) {
        return (byte) (left * right);
    }

    public Byte div(Byte left, Byte right) throws DBZException {
        checkDiv(left, right);
        return (byte) (left / right);
    }

    private void checkDiv(Byte left, Byte right) throws DBZException {
        if (right == 0) {
            throw new DBZException();
        }
    }

    public Byte mod(Byte left, Byte right) throws EvaluatingException {
        checkDiv(left, right);
        return (byte) (left % right);
    }

    public Byte negate(Byte x) {
        return (byte) -x;
    }

    public Byte abs(Byte x) throws OverflowException {
        return (byte) Math.abs(x);
    }

}
