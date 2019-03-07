package ru.job4j.jdbc.xslt;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class Entry {
    private int field;

    public Entry() {
    }

    public Entry(int field) {

        this.field = field;
    }

    public void setField(int field) {

        this.field = field;
    }

    public int getField() {
        return this.field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entry)) {
            return false;
        }
        Entry entry = (Entry) o;
        return getField() == entry.getField();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getField());
    }

    @Override
    public String toString() {
        return String.format("Entry {field = %1d}", this.field);
    }
}
