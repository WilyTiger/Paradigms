package expression;

import expression.exceptions.NegativeSqrtException;

import static java.lang.Math.sqrt;

public class CheckedSqrt extends AbstractUnaryOperation {
    public CheckedSqrt(TripleExpression exp) {
        super(exp);
    }

    private void check(int operand) throws NegativeSqrtException {
        if (operand < 0) {
            throw new NegativeSqrtException("negative sqrt operand");
        }
    }
    protected int doOperation(int operand) throws NegativeSqrtException {
        check(operand);
        return (int)sqrt(operand);
    }
}
