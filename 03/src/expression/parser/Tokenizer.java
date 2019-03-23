package expression.parser;

import expression.exceptions.*;
import expression.operation.Operation;

import java.util.*;

public class Tokenizer<T> {
    private String expression;
    private int position;
    private Token curToken;
    private T value;
    private int balance;
    private String name;
    private Operation<T> operation;

    final private static Set<Token> OPERATIONS = EnumSet.of(Token.ADD, Token.SUB, Token.DIV,
            Token.MUL, Token.MINUS, Token.MIN, Token.MAX, Token.ABS, Token.SQUARE, Token.LOW, Token.HIGH, Token.MOD);
    final private static Set<Token> UNARY_OPERATIONS = EnumSet.of(Token.ADD, Token.SUB, Token.DIV,
            Token.MUL, Token.MINUS, Token.MIN, Token.MAX, Token.ABS, Token.SQUARE, Token.LOW, Token.HIGH);


    final private static Map<String, Token> IDENTIFIERS = new HashMap<>();
    static {
        IDENTIFIERS.put("mod", Token.MOD);
        IDENTIFIERS.put("low", Token.LOW);
        IDENTIFIERS.put("high", Token.HIGH);
        IDENTIFIERS.put("abs", Token.ABS);
        IDENTIFIERS.put("square", Token.SQUARE);
        IDENTIFIERS.put("x", Token.VARIABLE);
        IDENTIFIERS.put("y", Token.VARIABLE);
        IDENTIFIERS.put("z", Token.VARIABLE);
    }


    public T getValue() {
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

    public Tokenizer(String expression, Operation<T> operation) {
        this.expression = expression;
        curToken = Token.BEGIN;
        position = 0;
        this.operation = operation;
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
            value = operation.getConst(number);
        } catch (IncorrectConstantException e) {
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
