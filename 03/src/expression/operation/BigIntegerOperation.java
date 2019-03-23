package expression.operation;

import expression.exceptions.*;

import java.math.BigInteger;

public class BigIntegerOperation implements Operation<BigInteger> {
    public BigInteger getConst(String number) throws IncorrectConstantException {
        try {
            return new BigInteger(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstantException();
        }
    }

    public BigInteger add(BigInteger left, BigInteger right) {
        return left.add(right);
    }

    public BigInteger sub(BigInteger left, BigInteger right) {
        return left.subtract(right);
    }

    public BigInteger mul(BigInteger left, BigInteger right) {
        return left.multiply(right);
    }

    public BigInteger div(BigInteger left, BigInteger right) throws DBZException {
        checkDiv(right);
        return left.divide(right);
    }

    public BigInteger mod(BigInteger left, BigInteger right) throws EvaluatingException {
        checkDiv(right);
        checkMod(right);
        return left.mod(right);
    }

    private void checkMod(BigInteger right) throws NegativeModExpressionException {
        if (right.compareTo(BigInteger.ZERO) < 0) {
            throw new NegativeModExpressionException();
        }
    }

    private void checkDiv(BigInteger right) throws DBZException {
        if (right.equals(BigInteger.ZERO)) {
            throw new DBZException();
        }
    }

    public BigInteger negate(BigInteger x) {
        return x.negate();
    }

    public BigInteger abs(BigInteger x) {
        return x.abs();
    }
}
