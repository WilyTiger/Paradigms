package expression.operation;

import expression.exceptions.*;

public class FloatOperation implements Operation<Float> {
    public Float getConst(String number) throws IncorrectConstantException {
        try {
            return (float) Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new IncorrectConstantException();
        }
    }

    public Float add(Float left, Float right) {
        return left + right;
    }

    public Float sub(Float left, Float right) {
        return left - right;
    }

    public Float mul(Float left, Float right) {
        return left * right;
    }

    public Float div(Float left, Float right) {
        return left / right;
    }

    public Float mod(Float left, Float right) throws EvaluatingException {
        return left % right;
    }

    public Float negate(Float x) {
        return -x;
    }

    public Float abs(Float x) {
        return Math.abs(x);
    }

}
