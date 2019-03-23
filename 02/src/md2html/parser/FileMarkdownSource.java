package md2html.parser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileMarkdownSource extends MarkdownSource {

    private final Reader reader;

    public FileMarkdownSource(final String fileName) throws MarkdownException {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            throw error("Error opening input file '%s': %s", fileName, e.getMessage());
        }
    }

    @Override
    protected char readChar() throws IOException {
        final int read = reader.read();
        return read == -1 ? END : (char) read;
    }
}
