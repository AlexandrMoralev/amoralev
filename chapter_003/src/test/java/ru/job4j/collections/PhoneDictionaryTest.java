package ru.job4j.collections;

import org.junit.Test;
import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * PhoneDictionaryTest
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class PhoneDictionaryTest {

    /**
     * Test. Searching by name.
     */
    @Test
    public void whenFindByName() {

        PhoneDictionary phones = new PhoneDictionary();

        phones.add(
                new Person("Alexandr", "Moralev", "321584", "NNovgorod")
        );
        phones.add(
                new Person("Petr", "Arsentev", "534872", "Bryansk")
        );

        List<Person> persons = phones.find("petr");
        assertThat(persons.iterator().next().getSurname(), is("Arsentev"));
    }

    /**
     * Test. Searching by surname.
     */
    @Test
    public void whenFindBySurname() {

        PhoneDictionary phones = new PhoneDictionary();

        phones.add(
                new Person("Alexandr", "Moralev", "321584", "NNovgorod")
        );
        phones.add(
                new Person("Petr", "Arsentev", "534872", "Bryansk")
        );

        List<Person> persons = phones.find("sent");
        assertThat(persons.iterator().next().getPhone(), is("534872"));
    }

    /**
     * Test. Searching by address.
     */
    @Test
    public void whenFindByAddress() {

        PhoneDictionary phones = new PhoneDictionary();

        phones.add(
                new Person("Alexandr", "Moralev", "321584", "NNovgorod")
        );
        phones.add(
                new Person("Petr", "Arsentev", "534872", "Bryansk")
        );

        List<Person> persons = phones.find("nn");
        assertThat(persons.iterator().next().getSurname(), is("Moralev"));

    }

    /**
     * Test. Searching by phone.
     */
    @Test
    public void whenFindByPhone() {

        PhoneDictionary phones = new PhoneDictionary();

        phones.add(
                new Person("Alexandr", "Moralev", "321584", "NNovgorod")
        );
        phones.add(
                new Person("Petr", "Arsentev", "534872", "Bryansk")
        );

        List<Person> persons = phones.find("58");

        assertThat(persons.iterator().next().getName(), is("Alexandr"));
    }
}
