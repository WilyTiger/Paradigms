package expression;

public class Low extends AbstractUnaryOperation {
    public Low(TripleExpression exp) {
        super(exp);
    }
    protected int doOperation(int operand) {
        int ans = 0;
        for (int i = 0; i <= 31; i++) {
            if ((operand & (1 << i)) >= 1) {
                ans = 1 << i;
                break;
            }
        }
        if (operand == Integer.MIN_VALUE) {
            ans = Integer.MIN_VALUE;
        }
        return ans;
    }
}
