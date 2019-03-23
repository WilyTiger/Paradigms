package expression;

public class High extends AbstractUnaryOperation {
    public High(TripleExpression exp) {
        super(exp);
    }
    protected int doOperation(int operand) {
        int ans = 0;
        for (int i = 31; i >= 0; i--) {
            if ((operand & (1 << i)) >= 1) {
                ans = 1 << i;
                break;
            }
        }
        if (operand < 0)
            ans = Integer.MIN_VALUE;
        return ans;
    }
}
