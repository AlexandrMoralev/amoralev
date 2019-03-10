package ru.job4j.jdbc.xslt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

/**
 * Entries - wrapper for the List of Entry to convert XML-data
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "entries")
public class Entries {

    @XmlElement(name = "entry")
    private List<Entry> entries;

    public Entries() {
    }

    public Entries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        return this.entries == null ? Collections.emptyList() : this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }
}
