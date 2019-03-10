package ru.job4j.jdbc.xslt;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

/**
 * SAXEntriesParser - SAX parser
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class SAXEntriesParser {
    private File source;

    /**
     * Constructs SAXEntriesParser with an input file reference
     *
     * @param source file to be parsed
     */
    public SAXEntriesParser(final File source) {
        this.source = source;
    }

    /**
     * Parses the input file,
     * calculates the arithmetic sum of the values of all attributes using Counter handler
     *
     * @return long the arithmetic sum of the values of all attributes
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public long parseSum() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        Counter counter = new Counter();
        xmlReader.setContentHandler(counter);
        xmlReader.parse(this.source.getAbsolutePath());
        return counter.getSum();
    }
}
