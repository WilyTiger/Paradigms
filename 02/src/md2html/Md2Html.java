package md2html;

import md2html.parser.FileMarkdownSource;
import md2html.parser.MarkdownException;
import md2html.parser.MarkdownParser;

import java.io.*;

public class Md2Html {
    public static void main(String[] args) {
        try {
            PrintStream output = new PrintStream(new FileOutputStream(args[1]));
            MarkdownParser parser = new MarkdownParser(new FileMarkdownSource(args[0]));
            output.println(parser.parse());
        } catch (FileNotFoundException err) {
            System.out.println("Cannot find input file " + args[0]);
        } catch (MarkdownException err) {

        }

    }
}