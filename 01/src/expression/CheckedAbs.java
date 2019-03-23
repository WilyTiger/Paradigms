package expression;


import expression.exceptions.OverflowException;

import static java.lang.Math.abs;
public class CheckedAbs extends AbstractUnaryOperation {
    public CheckedAbs(TripleExpression exp) {
        super(exp);
    }

    void check(int x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    protected int doOperation(int x) throws OverflowException {
        check(x);
        return abs(x);
    }
}
