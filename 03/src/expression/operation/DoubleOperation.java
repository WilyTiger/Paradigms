package expression.operation;

import expression.exceptions.*;

public class DoubleOperation implements Operation<Double> {
    public Double getConst(String number) throws IncorrectConstantException {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstantException();
        }
    }

    public Double add(Double left, Double right) {
        return left + right;
    }

    public Double sub(Double left, Double right) {
        return left - right;
    }

    public Double mul(Double left, Double right) {
        return left * right;
    }

    public Double div(Double left, Double right) {
        return left / right;
    }

    public Double mod(Double left, Double right) {
        return left % right;
    }

    public Double negate(Double x) {
        return -x;
    }

    public Double abs(Double x) {
        return Math.abs(x);
    }

}
