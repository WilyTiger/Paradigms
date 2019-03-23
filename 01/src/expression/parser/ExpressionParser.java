package expression.parser;

import expression.*;
import expression.exceptions.MissingCloseBracket;
import expression.exceptions.ParsingException;

public class ExpressionParser implements Parser {
    private Tokenizer tokenizer;

    private TripleExpression unary() throws ParsingException {
        tokenizer.nextToken();
        TripleExpression res;
        switch (tokenizer.getCurToken()) {
            case CONST:
                res = new Const(tokenizer.getValue());
                tokenizer.nextToken();
                break;
            case VARIABLE:
                res = new Variable(tokenizer.getName());
                tokenizer.nextToken();
                break;
            case MINUS:
                res = new CheckedNegate(unary());
                break;
            case SQRT:
                res = new CheckedSqrt(unary());
                break;
            case ABS:
                res = new CheckedAbs(unary());
                break;
            case LOW:
                res = new Low(unary());
                break;
            case HIGH:
                res = new High(unary());
                break;
            case OPEN_BRACE:
                int pos = tokenizer.getPosition();
                res = addAndSub();
                if (tokenizer.getCurToken() != Token.CLOSE_BRACE) {
                    throw new MissingCloseBracket(tokenizer.getExpression(), pos - 1);
                }
                tokenizer.nextToken();
                break;
            default:
                return new Const(0);
        }
        return res;
    }


    private TripleExpression mulAndDiv() throws ParsingException {
        TripleExpression res = unary();
        while (true) {
            switch (tokenizer.getCurToken()) {
                case MUL:
                    res = new CheckedMultiply(res, unary());
                    break;
                case DIV:
                    res = new CheckedDivide(res, unary());
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression addAndSub() throws ParsingException {
        TripleExpression res = mulAndDiv();
        while (true) {
            switch (tokenizer.getCurToken()) {
                case ADD:
                    res = new CheckedAdd(res, mulAndDiv());
                    break;
                case SUB:
                    res = new CheckedSubtract(res, mulAndDiv());
                    break;
                default:
                    return res;
            }
        }
    }

    public TripleExpression parse(String exp) throws ParsingException {
        tokenizer = new Tokenizer(exp);
        return addAndSub();
    }
}
