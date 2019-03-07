package ru.job4j.jdbc.xslt;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

@XmlRootElement
public class Entries {

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
