package ru.job4j.jdbc.xslt;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Counter - Handler for SAX parser
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Counter extends DefaultHandler {

    private long sum;

    /**
     * Constructs Counter instance
     */
    public Counter() {
        this.sum = 0;
    }

    @Override
    public void startElement(final String uri,
                             final String localName,
                             final String qName,
                             final Attributes attributes
    ) throws SAXException {
        if ("entry".equals(qName)) {
            sum += Integer.valueOf(attributes.getValue("field"));
        }
    }

    public long getSum() {
        return this.sum;
    }
}
