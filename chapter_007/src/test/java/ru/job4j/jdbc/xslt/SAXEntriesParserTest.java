package ru.job4j.jdbc.xslt;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * SAXEntriesParserTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SAXEntriesParserTest {
    private final int numberOfEntry = 10000;
    private final String sourceFilepath = "./entries.xml";
    private final String destFilepath = "./entriesConverted.xml";
    private final String schemeFilepath = "../scheme.scm";

    @Test
    public void whenParseSumShouldReturnCorrectAnswer() throws SQLException, TransformerException, IOException, SAXException, ParserConfigurationException {
        final StoreSQL storeSQL = new StoreSQL(new Config());
        storeSQL.generate(numberOfEntry);

        final Entries entries = new Entries(storeSQL.load());

        final StoreXML storeXML = new StoreXML(new File(sourceFilepath));
        storeXML.save(entries.getEntries());

        final ConvertXSQT convert = new ConvertXSQT();
        final File destFile = new File(destFilepath);
        convert.convert(new File(sourceFilepath),
                destFile,
                new File(schemeFilepath)
        );

        final SAXEntriesParser parser = new SAXEntriesParser(destFile);
        assertThat(parser.parseSum(), is(getSumFromZeroToValue(numberOfEntry)));
    }

    private long getSumFromZeroToValue(int value) {
        long result = 0;
        for (int i = 1; i <= value; i++) {
            result += i;
        }
        return result;
    }
}
