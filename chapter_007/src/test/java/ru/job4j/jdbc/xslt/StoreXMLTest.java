package ru.job4j.jdbc.xslt;

import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * StoreXMLTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class StoreXMLTest {

    @Test
    public void whenSavingListOfItemsThenMarshalledXMLContainsItems() throws SQLException, JAXBException {
        testMarshalling(7, "./storeFirst.xml");
    }

    @Test
    public void whenSavingEmptyListThenFileIsEmpty() throws SQLException, JAXBException {
        testMarshalling(-2, "./storeSecond.xml");
    }

    private void testMarshalling(int items, String filePath) throws JAXBException, SQLException {
        final StoreSQL store = new StoreSQL(new Config());
        store.generate(items);
        final List<Entry> entryList = store.load();

        final File xmlFile = new File(filePath);
        final StoreXML storeXML = new StoreXML(xmlFile);
        storeXML.save(entryList);

        final Entries result;
        JAXBContext jaxbContext = JAXBContext.newInstance(Entries.class, Entry.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        result = (Entries) unmarshaller.unmarshal(xmlFile);

        store.close();
        assertThat(result.getEntries(), is(entryList));
    }
}
