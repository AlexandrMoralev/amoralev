package ru.job4j.jdbc.xslt;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

/**
 * StoreXML - преобразует List<Entry> в XML и записывает их в файл,
 * передает имя XML файла в ConvertXSQT
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreXML {
    private final File target;
    // DEFAULT values
    private static final String FILE_PATH = "./";
    private static final String FILE_NAME = "file.xml";

    public StoreXML() {
        this(new File(FILE_PATH + FILE_NAME));
    }

    public StoreXML(final File target) {
        this.target = target;
    }

    //сохраняет данные из list в файл target.
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
            e.printStackTrace();
        }
    }
}
