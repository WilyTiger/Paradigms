package expression;


import static java.lang.Math.min;
public class Min extends AbstractBinaryOperation {
    public Min(TripleExpression left, TripleExpression right) {
        super(left, right);
    }

    protected int doOperation(int left, int right) {
        return min(left, right);
    }
}
