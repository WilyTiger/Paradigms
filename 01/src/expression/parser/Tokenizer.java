package expression.parser;

import expression.exceptions.*;

import java.util.*;

public class Tokenizer {
    private String expression;
    private int position;
    private Token curToken;
    private int value;
    private int balance;
    private String name;
    final private static Set<Token> OPERATIONS = EnumSet.of(Token.ADD, Token.SUB, Token.DIV,
            Token.MUL, Token.MINUS, Token.MIN, Token.MAX, Token.ABS, Token.SQRT, Token.LOW, Token.HIGH);
    final private static Set<Token> UNARY_OPERATIONS = EnumSet.of(Token.ADD, Token.SUB, Token.DIV,
            Token.MUL, Token.MINUS, Token.MIN, Token.MAX, Token.ABS, Token.SQRT, Token.LOW, Token.HIGH);


    final private static Map<String, Token> IDENTIFIERS = new HashMap<>();
    static {
        IDENTIFIERS.put("max", Token.MAX);
        IDENTIFIERS.put("min", Token.MIN);
        IDENTIFIERS.put("low", Token.LOW);
        IDENTIFIERS.put("high", Token.HIGH);
        IDENTIFIERS.put("abs", Token.ABS);
        IDENTIFIERS.put("sqrt", Token.SQRT);
        IDENTIFIERS.put("x", Token.VARIABLE);
        IDENTIFIERS.put("y", Token.VARIABLE);
        IDENTIFIERS.put("z", Token.VARIABLE);
    }


    public int getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public Token getCurToken() {
        return curToken;
    }

    public Tokenizer(String expression) {
        this.expression = expression;
        curToken = Token.BEGIN;
        position = 0;
    }

    private void skipSpaces() {
        while (position < expression.length() && Character.isWhitespace(expression.charAt(position))) {
            position++;
        }
    }

    private String getNumber() {
        int startPosition = position;
        while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
            position++;
        }
        position--;
        return expression.substring(startPosition, position + 1);
    }

    private void getConst(String number) throws ParsingException {
        try {
            value = Integer.parseInt(number);
        } catch (NumberFormatException err) {
            throw new IllegalConstantException(number, expression, position - number.length() + 1);
        }
    }

    private String getString() throws UnknownSymbolException {
        if (!Character.isLetter(expression.charAt(position))) {
            throw new UnknownSymbolException(expression, position);
        }
        int startPosition = position;
        while (position < expression.length() && Character.isLetterOrDigit(expression.charAt(position))) {
            position++;
        }
        position--;
        return expression.substring(startPosition, position + 1);
    }

    private void checkOperand() throws ParsingException {
        if (OPERATIONS.contains(curToken) || curToken == Token.OPEN_BRACE
                || curToken == Token.BEGIN) {
            throw new MissingOperandException(expression, position);
        }
    }

    private void checkOperation() throws ParsingException {
        if (curToken == Token.CONST || curToken == Token.CLOSE_BRACE
                || curToken == Token.VARIABLE) {
            throw new MissingOperationException(expression, position);
        }
    }


    public void nextToken() throws ParsingException {
        skipSpaces();
        if (position >= expression.length()) {
            checkOperand();
            curToken = Token.END;
            return;
        }
        char currentSymbol = expression.charAt(position);
        switch (currentSymbol) {
            case '-':
                if (curToken == Token.CONST || curToken == Token.VARIABLE
                        || curToken == Token.CLOSE_BRACE) {
                    curToken = Token.SUB;
                } else {
                    if (position + 1 >= expression.length()) {
                        throw new MissingOperandException(expression, position);
                    }

                    if (Character.isDigit(expression.charAt(position + 1))) {
                        position++;
                        getConst("-" + getNumber());
                        curToken = Token.CONST;
                    } else {
                        curToken = Token.MINUS;
                    }
                }
                break;
            case '+':
                checkOperand();
                curToken = Token.ADD;
                break;
            case '*':
                checkOperand();
                curToken = Token.MUL;
                break;
            case '/':
                checkOperand();
                curToken = Token.DIV;
                break;
            case '(':
                checkOperation();
                balance++;
                curToken = Token.OPEN_BRACE;
                break;
            case ')':
                checkOperand();
                balance--;
                if (balance < 0) {
                    throw new ExcessCloseBracketException(expression, position);
                }
                curToken = Token.CLOSE_BRACE;
                break;
            default:
                if (Character.isDigit(currentSymbol)) {
                    getConst(getNumber());
                    curToken = Token.CONST;
                } else {
                    String identifier = getString();
                    if (!IDENTIFIERS.containsKey(identifier)) {
                        throw new UnknownIdentifierException(identifier, expression, position - identifier.length() + 1);
                    }
                    Token next = IDENTIFIERS.get(identifier);
                    if (next == Token.VARIABLE) {
                        name = identifier;
                    } else {
                        if (!UNARY_OPERATIONS.contains(next))
                            checkOperand();
                    }
                    curToken = next;
                }
        }
        position++;
    }
}
