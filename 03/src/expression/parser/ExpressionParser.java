package expression.parser;

import expression.*;
import expression.exceptions.MissingCloseBracket;
import expression.exceptions.ParsingException;
import expression.operation.Operation;

public class ExpressionParser<T> implements Parser<T> {
    private Tokenizer<T> tokenizer;
    private Operation<T> operation;

    public ExpressionParser(Operation<T> operation) {
        this.operation = operation;
    }

    private TripleExpression<T> unary() throws ParsingException {
        tokenizer.nextToken();
        TripleExpression<T> res;
        switch (tokenizer.getCurToken()) {
            case CONST:
                res = new Const<T>(tokenizer.getValue());
                tokenizer.nextToken();
                break;
            case VARIABLE:
                res = new Variable<T>(tokenizer.getName());
                tokenizer.nextToken();
                break;
            case MINUS:
                res = new Negate<T>(unary(), operation);
                break;
            case ABS:
                res = new Abs<T>(unary(), operation);
                break;
            case SQUARE:
                res = new Square<T>(unary(), operation);
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
                throw new ParsingException("Incorrect Expression\n" + tokenizer.getExpression());
        }
        return res;
    }


    private TripleExpression<T> mulAndDivAndMod() throws ParsingException {
        TripleExpression<T> res = unary();
        while (true) {
            switch (tokenizer.getCurToken()) {
                case MUL:
                    res = new Multiply<T>(res, unary(), operation);
                    break;
                case DIV:
                    res = new Divide<T>(res, unary(), operation);
                    break;
                case MOD:
                    res = new Mod<T>(res, unary(), operation);
                    break;
                default:
                    return res;
            }
        }
    }

    private TripleExpression<T> addAndSub() throws ParsingException {
        TripleExpression<T> res = mulAndDivAndMod();
        while (true) {
            switch (tokenizer.getCurToken()) {
                case ADD:
                    res = new Add<T>(res, mulAndDivAndMod(), operation);
                    break;
                case SUB:
                    res = new Subtract<T>(res, mulAndDivAndMod(), operation);
                    break;
                default:
                    return res;
            }
        }
    }

    public TripleExpression<T> parse(String exp) throws ParsingException {
        tokenizer = new Tokenizer<T>(exp, operation);
        return addAndSub();
    }
}
