package md2html.parser;

public enum Token {
    MARK("<mark>", "</mark>"),
    UNDER_LINE("<u>", "</u>"),
    STRONG("<strong>", "</strong>"),
    STRIKE("<s>", "</s>"),
    EM("<em>", "</em>"),
    CODE("<code>", "</code>"),
    SINGLE(null, null),
    BEGIN(null, null),
    NONE(null, null),
    SCREENING(null, null),
    A("<a", "</a>"),
    IMG("<img", ">"),
    LINK(null, null);
    String openTag;
    String closeTag;
    Token(String open, String close) {
        openTag = open;
        closeTag = close;
    }
}
