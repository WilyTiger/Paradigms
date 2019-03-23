package md2html.parser;

import org.junit.Assert;
import org.junit.Test;


public class MarkdownParserTest {
    @Test
    public void testHeader() throws MarkdownException {
        testParse(
                "# Заголовок первого уровня\n\n",
                "<h1>Заголовок первого уровня</h1>"
        );
        testParse(
                "## Второго\n\n",
                "<h2>Второго</h2>"
        );
        testParse(
                "### Третьего ## уровня\n\n",
                "<h3>Третьего ## уровня</h3>"
        );
        testParse(
                "#### Четвертого\n# Все еще четвертого\n\n",
                "<h4>Четвертого\n# Все еще четвертого</h4>"
        );
    }

    @Test
    public void testParagraph() throws MarkdownException {
        testParse(
                "Этот абзац текста,\nсодержит две строки.",
                "<p>Этот абзац текста,\nсодержит две строки.</p>"
        );
        testParse(
                "#И это не заголовок.\n\n",
                "<p>#И это не заголовок.</p>"
        );
        testParse(
                "###### Заголовки могут быть многострочными\n(и с пропуском заголовков предыдущих уровней)\n\n",
                "<h6>Заголовки могут быть многострочными\n(и с пропуском заголовков предыдущих уровней)</h6>"
        );
        testParse(
                "###привет мир",
                "<p>###привет мир</p>"
        );
        testParse(
                "# # #привет мир",
                "<h1># #привет мир</h1>"
        );
        testParse(
                "\n\n\nЛишние пустые строки должны игнорироваться.\n\n\n\n",
                "<p>Лишние пустые строки должны игнорироваться.</p>"
        );
    }

    @Test
    public void testEm() throws MarkdownException {
        testParse(
                "*Слабое выделение*",
                "<p><em>Слабое выделение</em></p>"
        );

        testParse(
                "# *Слабое выделение*",
                "<h1><em>Слабое выделение</em></h1>"
        );
        testParse(
                "### *Слабое выделение*",
                "<h3><em>Слабое выделение</em></h3>"
        );
        testParse(
                "_Слабое выделение_",
                "<p><em>Слабое выделение</em></p>"
        );

        testParse(
                "# _Слабое выделение_",
                "<h1><em>Слабое выделение</em></h1>"
        );
        testParse(
                "### _Слабое выделение_",
                "<h3><em>Слабое выделение</em></h3>"
        );

    }

    @Test
    public void testCode() throws MarkdownException {
        testParse(
                "`выделение`",
                "<p><code>выделение</code></p>"
        );
        testParse(
                "**`выделение`**",
                "<p><strong><code>выделение</code></strong></p>"
        );
        testParse(
                "## `выделение`",
                "<h2><code>выделение</code></h2>"
        );
        testParse(
                "#### **`выделение`**",
                "<h4><strong><code>выделение</code></strong></h4>"
        );
        testParse(
                "#### **`выделение`**",
                "<h4><strong><code>выделение</code></strong></h4>"
        );
        testParse(
                "Мы все любим *выделять* текст _разными_ способами.\n**Сильное выделение**, используется гораздо реже,\nно __почему бы и нет__?\nНемного --зачеркивания-- еще ни кому не вредило.\nКод представляется элементом `code`.\n\n",
                "<p>Мы все любим <em>выделять</em> текст <em>разными</em> способами.\n<strong>Сильное выделение</strong>, используется гораздо реже,\nно <strong>почему бы и нет</strong>?\nНемного <s>зачеркивания</s> еще ни кому не вредило.\nКод представляется элементом <code>code</code>.</p>"
        );
        testParse("Мы все любим *выделять* текст",
                "<p>Мы все любим <em>выделять</em> текст</p>");

    }

    private void testParse(final String input, final String result) throws MarkdownException {
        try {
            Assert.assertEquals(input, result, parse(input));
        } catch (final MarkdownException e) {
            System.err.format("Error while parsing '%s'%n", input);
            throw e;
        }
    }


    @Test
    public void testStrong() throws MarkdownException {
        testParse(
                "**Сильное выделение**",
                "<p><strong>Сильное выделение</strong></p>"
        );

        testParse(
                "# **Сильное выделение**",
                "<h1><strong>Сильное выделение</strong></h1>"
        );
        testParse(
                "### **Сильное выделение**",
                "<h3><strong>Сильное выделение</strong></h3>"
        );
        testParse(
                "__Сильное выделение__",
                "<p><strong>Сильное выделение</strong></p>"
        );
        testParse(
                "# __Сильное выделение__",
                "<h1><strong>Сильное выделение</strong></h1>"
        );
        testParse(
                "### __Сильное выделение__",
                "<h3><strong>Сильное выделение</strong></h3>"
        );
    }

    @Test
    public void testAll() throws MarkdownException {
        testParse("# Без перевода строки в конце", "<h1>Без перевода строки в конце</h1>");
        testParse("# Один перевод строки в конце\n", "<h1>Один перевод строки в конце</h1>");
        testParse("# Два перевода строки в конце\n\n", "<h1>Два перевода строки в конце</h1>");
        testParse(
                "Выделение может *начинаться на одной строке,\n а заканчиваться* на другой",
                "<p>Выделение может <em>начинаться на одной строке,\n а заканчиваться</em> на другой</p>"
        );
        testParse("# *Выделение* и `код` в заголовках", "<h1><em>Выделение</em> и <code>код</code> в заголовках</h1>");
        testParse(
                "Знаете ли вы, что в Markdown, одиночные * и _\nне означают выделение?\nОни так же могут быть заэкранированы\nпри помощи обратного слэша: \\*.",
                "<p>Знаете ли вы, что в Markdown, одиночные * и _\nне означают выделение?\nОни так же могут быть заэкранированы\nпри помощи обратного слэша: *.</p>"
        );
    }

    @Test
    public void testLink() throws MarkdownException {
        testParse("[ссылок с _выделением_](https://kgeorgiy.info)", "<p><a href='https://kgeorgiy.info'>ссылок с <em>выделением</em></a></p>");
        testParse("[ссылка с __выделением__](https://kgeorgiy.info)", "<p><a href='https://kgeorgiy.info'>ссылка с <strong>выделением</strong></a></p>");
        testParse("[ссылка без выделения](https://kgeorgiy.info)", "<p><a href='https://kgeorgiy.info'>ссылка без выделения</a></p>");
        testParse("[ссылка без выделения](https://hello__kgeorgiy.info)", "<p><a href='https://hello__kgeorgiy.info'>ссылка без выделения</a></p>");
        testParse("_выделение [ссылка с __выделением__](https://kgeorgiy.info)_", "<p><em>выделение <a href='https://kgeorgiy.info'>ссылка с <strong>выделением</strong></a></em></p>");
    }

    @Test
    public void testImage() throws MarkdownException {
        testParse("![картинок](http://www.ifmo.ru/images/menu/small/p10.jpg)", "<p><img alt='картинок' src='http://www.ifmo.ru/images/menu/small/p10.jpg'></p>");
        testParse("![картинка](https://kgeorgiy.info)", "<p><img alt='картинка' src='https://kgeorgiy.info'></p>");
        testParse("![картинка с __псевдо-выделением__](https://kgeorgiy.info)", "<p><img alt='картинка с __псевдо-выделением__' src='https://kgeorgiy.info'></p>");
    }

    @Test
    public void testLMark() throws MarkdownException {
        testParse("~выделение~", "<p><mark>выделение</mark></p>");
    }

    private String parse(final String input) throws MarkdownException {
        return new MarkdownParser(new StringMarkdownSource(input)).parse();
    }
}
