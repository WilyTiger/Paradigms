package md2html.parser;

import java.io.IOException;

public abstract class MarkdownSource {
    public static char END = '\0';

    protected int pos;
    protected int line = 1;
    protected int posInLine;
    private char cur;
    private char prev;

    protected abstract char readChar() throws IOException;


    public char getPrevChar() {
        return prev;
    }

    public char getChar() {
        return cur;
    }

    public char nextChar() throws MarkdownException {
        try {
            if (cur == '\n') {
                line++;
                posInLine = 0;
            }
            prev = cur;
            cur = readChar();
            pos++;
            posInLine++;
            return cur;
        } catch (final IOException e) {
            throw error("Source read error", e.getMessage());
        }
    }

    public MarkdownException error(final String format, final Object... args) throws MarkdownException {
        return new MarkdownException(line, posInLine, String.format("%d:%d: %s", line, posInLine, String.format(format, args)));
    }
}
