package ru.job4j.jdbc.xslt;

import javax.xml.bind.annotation.*;
import java.util.List;

public class XmlUsage {
    @XmlRootElement
    public static class Entries {
        private List<Entry> entries;

        public Entries () {
        }

        public Entries(List<Entry> entries) {
            this.entries = entries;
        }

        public List<Entry> getEntries() {
            return this.entries;
        }

        public void setEntries(List<Entry> entries) {
            this.entries = entries;
        }
    }

    @XmlRootElement
    public static class Entry {
        private int value;

        public Entry() {}

        public Entry(int value) {
            this.value = value;
        }

        public void setValue(int value) {
            this.value = value;
        }

    }
}
