package md2html.parser;

import java.util.ArrayList;
import java.util.HashMap;

public class MarkdownParser {
    private static final ArrayList<String> DOUBLE_EMPHASIS = new ArrayList<>();
    private static final ArrayList<String> EMPHASIS = new ArrayList<>();
    private static final HashMap<String, Token> SYMBOLS_TO_TOKEN = new HashMap<>();

    static {
        EMPHASIS.add("*");
        EMPHASIS.add("_");
        EMPHASIS.add("`");
        EMPHASIS.add("~");
        DOUBLE_EMPHASIS.add("**");
        DOUBLE_EMPHASIS.add("++");
        DOUBLE_EMPHASIS.add("__");
        DOUBLE_EMPHASIS.add("--");

        SYMBOLS_TO_TOKEN.put("**", Token.STRONG);
        SYMBOLS_TO_TOKEN.put("++", Token.UNDER_LINE);
        SYMBOLS_TO_TOKEN.put("__", Token.STRONG);
        SYMBOLS_TO_TOKEN.put("--", Token.STRIKE);
        SYMBOLS_TO_TOKEN.put("*", Token.EM);
        SYMBOLS_TO_TOKEN.put("_", Token.EM);
        SYMBOLS_TO_TOKEN.put("`", Token.CODE);
        SYMBOLS_TO_TOKEN.put("~", Token.MARK);
    }

    private final MarkdownSource source;

    public MarkdownParser(MarkdownSource source) {
        this.source = source;
    }

    public String parse() throws MarkdownException {
        source.nextChar();
        source.nextChar();
        StringBuilder result = new StringBuilder();
        while (true) {
            skipLines();
            if (testEnd())
                break;
            if (result.length() > 0) {
                result.append('\n');
            }
            result.append(parseHeaderOrParagraph());
        }
        return result.toString();
    }

    private boolean test(char c1, char c2) {
        return source.getPrevChar() == c1 && source.getChar() == c2;
    }

    private boolean testPrev(char c) {
        return source.getPrevChar() == c;
    }

    private boolean testCur(char c) {
        return source.getChar() == c;
    }

    private boolean testNext(String s) throws MarkdownException {
        if (s.length() == 1) {
            return testNext(s.charAt(0));
        }
        if (s.length() == 2) {
            return testNext(s.charAt(0), s.charAt(1));
        }
        return false;
    }

    private boolean testNext(char c) throws MarkdownException {
        if (source.getPrevChar() == c) {
            source.nextChar();
            return true;
        } else {
            return false;
        }
    }

    private boolean testNext(char c1, char c2) throws MarkdownException {
        if (source.getPrevChar() == c1 && source.getChar() == c2) {
            source.nextChar();
            source.nextChar();
            return true;
        } else {
            return false;
        }
    }

    private boolean testEnd() {
        return (source.getPrevChar() == '\0' || source.getPrevChar() == '\n') && source.getChar() == '\0';
    }

    private StringBuilder parseHeaderOrParagraph() throws MarkdownException {
        int cnt = 0;
        StringBuilder result = new StringBuilder();
        StringBuilder pounds = new StringBuilder();
        while (testPrev('#')) {
            cnt++;
            source.nextChar();
            pounds.append('#');
        }

        StringBuilder open = new StringBuilder();
        StringBuilder close = new StringBuilder();
        if (cnt == 0 || !testPrev(' ')) {
            open.append("<p>");
            close.append("</p>");
            if (cnt != 0) {
                open.append(pounds);
            }
        } else {
            open.append("<h" + cnt + ">");
            close.append("</h" + cnt + ">");
            skipSpaces();
        }

        result.append(open);
        result.append(parseText(Token.BEGIN));
        result.append(close);
        return result;
    }


    private Token getNext(Token current) throws MarkdownException {
        if (testNext('!', '[')) {
            return Token.IMG;
        }
        if (testNext('[')) {
            return Token.A;
        }
        if ((current == Token.IMG || current == Token.A) && testNext(']')) {
            return current;
        }

        if (current == Token.LINK && testNext(')')) {
            return Token.LINK;
        }

        if (current == Token.LINK || current == Token.IMG) {
            return Token.NONE;
        }

        for (String symbols : DOUBLE_EMPHASIS) {
                if (testNext(symbols)) {
                    return SYMBOLS_TO_TOKEN.get(symbols);
                }
            }

            for (String symbols : EMPHASIS) {
                if (test('\\', symbols.charAt(0))) {
                    source.nextChar();
                    return Token.NONE;
                }
                if (testPrev(symbols.charAt(0))) {
                    Token next = SYMBOLS_TO_TOKEN.get(symbols);
                    if (current == next) {
                        source.nextChar();
                        return next;
                    }
                    if (testCur(' ') || testCur('\n')) {
                        return Token.SINGLE;
                    }
                    source.nextChar();
                    return next;
                }
            }

            return Token.NONE;
    }

    private StringBuilder parseText(Token current) throws MarkdownException {
        StringBuilder result = new StringBuilder();
        while (!(testNext('\n', '\n') || testEnd())) {
            Token next = getNext(current);
            if (next == current) {
                return result;
            }
            if (next == Token.A) {
                result.append(parseLink());
                continue;
            }
            if (next == Token.IMG) {
                result.append(parseImage());
                continue;
            }
            if (next == Token.SINGLE) {
                result.append(newSymbol());
                result.append(newSymbol());
                continue;
            }
            if (next == Token.NONE) {
                result.append(newSymbol());
                continue;
            }
            result.append(getTag(next));
        }
        return result;
    }

    private StringBuilder parseLink() throws MarkdownException {
        StringBuilder result = new StringBuilder();
        result.append(Token.A.openTag + " href='");
        StringBuilder linkText = parseText(Token.A);
        source.nextChar();
        StringBuilder link = parseText(Token.LINK);
        result.append(link + "'>");
        result.append(linkText);
        result.append(Token.A.closeTag);
        return result;
    }

    private StringBuilder parseImage() throws MarkdownException {
        StringBuilder result = new StringBuilder();
        result.append(Token.IMG.openTag + " alt='");
        StringBuilder linkText = parseText(Token.IMG);
        source.nextChar();
        StringBuilder link = parseText(Token.LINK);
        result.append(linkText + "' src='");
        result.append(link + "'");
        result.append(Token.IMG.closeTag);
        return result;
    }

    private StringBuilder getTag(Token t) throws MarkdownException {
        StringBuilder result = new StringBuilder();
        result.append(t.openTag);
        result.append(parseText(t));
        result.append(t.closeTag);
        return result;
    }

    private String newSymbol() throws MarkdownException {
        String result = "";
        result += source.getPrevChar();
        if (source.getPrevChar() == '<') {
            result = "&lt;";
        }
        if (source.getPrevChar() == '>') {
            result = "&gt;";
        }
        if (source.getPrevChar() == '&') {
            result = "&amp;";
        }
        source.nextChar();
        return result;
    }


    private void skipLines() throws MarkdownException {
        while (source.getPrevChar() == '\n') {
            source.nextChar();
        }
    }

    private void skipSpaces() throws MarkdownException {
        while (Character.isWhitespace(source.getPrevChar())) {
            source.nextChar();
        }
    }
}
