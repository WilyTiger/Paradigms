package expression.generic;

import expression.TripleExpression;
import expression.exceptions.EvaluatingException;
import expression.exceptions.ParsingException;
import expression.operation.*;
import expression.parser.ExpressionParser;

import java.util.HashMap;
import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final static Map<String, Operation<?>> MODES = new HashMap<>();

    static {
        MODES.put("i", new IntegerOperation(true));
        MODES.put("u", new IntegerOperation(false));
        MODES.put("d", new DoubleOperation());
        MODES.put("f", new FloatOperation());
        MODES.put("b", new ByteOperation());
        MODES.put("bi", new BigIntegerOperation());
    }

    private <T> Object[][][] answer(Operation<T> operation, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        ExpressionParser<T> parser = new ExpressionParser<>(operation);
        TripleExpression<T> parsedExpression;
        Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        try {
            parsedExpression = parser.parse(expression);
        } catch (ParsingException e) {
            e.printStackTrace();
            return result;
        }

        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    try {
                        String x = Integer.toString(i);
                        String y = Integer.toString(j);
                        String z = Integer.toString(k);
                        result[i - x1][j - y1][k - z1] = parsedExpression.evaluate(operation.getConst(x), operation.getConst(y), operation.getConst(z));
                    } catch (EvaluatingException  e) {
                        //e.printStackTrace();
                        result[i - x1][j - y1][k - z1] = null;
                    }
                }
            }
        }
        return result;
    }

    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        return answer(MODES.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }
}
