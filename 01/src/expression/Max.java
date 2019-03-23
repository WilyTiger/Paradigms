package expression;


import static java.lang.Math.max;
public class Max extends AbstractBinaryOperation {
    public Max(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    protected int doOperation(int left, int right) {
        return max(left, right);
    }
}
