package ru.job4j.jdbc.xslt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

/**
 * StoreXML - converts the List of DB-Items to XML-file
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreXML {

    private final static Logger LOG = LogManager.getLogger(StoreXML.class);
    private static final String FILE_PATH = "./";
    private static final String FILE_NAME = "file.xml";
    private final File target;

    /**
     * StoreXML instance constructor with default filepath parameters
     */
    public StoreXML() {
        this(new File(FILE_PATH + FILE_NAME));
    }

    /**
     * Constructs StoreXML instance with an output file reference
     *
     * @param target
     */
    public StoreXML(final File target) {
        this.target = target;
    }

    /**
     * Saves the list of db-items to an output XML-file, using JAXB marshalling
     *
     * @param list notnull List of Entry
     */
    public void save(final List<Entry> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        Entries entries = new Entries(list);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class, Entry.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(entries, this.target);
        } catch (JAXBException e) {
            LOG.error("Marshalling error", e);
            throw new RuntimeException(e);
        }
    }
}
