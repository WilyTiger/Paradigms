package md2html.parser;

public class StringMarkdownSource extends MarkdownSource {
    private final String data;

    public StringMarkdownSource(final String data) throws MarkdownException {
        this.data = data + END + END;
    }

    @Override
    protected char readChar() {
        return data.charAt(pos);
    }
}