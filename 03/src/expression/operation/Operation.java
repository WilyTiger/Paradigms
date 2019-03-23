package expression.operation;

import expression.exceptions.*;

public interface Operation<T> {
    T getConst(String s) throws IncorrectConstantException;
    T add(T left, T right) throws OverflowException;
    T sub(T left, T right) throws OverflowException;
    T mul(T left, T right) throws OverflowException;
    T div(T left, T right) throws EvaluatingException;
    T mod(T left, T right) throws EvaluatingException;
    T negate(T x) throws OverflowException;
    T abs(T x) throws OverflowException;
    default T square(T x) throws OverflowException {
        return mul(x, x);
    }
}
