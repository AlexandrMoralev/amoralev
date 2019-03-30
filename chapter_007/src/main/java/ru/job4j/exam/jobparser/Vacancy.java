package ru.job4j.exam.jobparser;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Vacancy
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Vacancy {
    private final String name;
    private final String description;
    private final String link;
    private final Timestamp created;

    public Vacancy(final String name,
                   final String description,
                   final String link,
                   final Timestamp created
    ) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.created = created;
    }

    public Vacancy(final String name,
                   final String description,
                   final String link,
                   final String created
    ) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.created = convertToTimestamp(created);
    }

    private Timestamp convertToTimestamp(final String created) {
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getCreatedAsString() {
        return this.created.toString();
    }

    public Timestamp getCreatedAsTimeStamp() {
        return this.created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vacancy)) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return getName().equals(vacancy.getName())
                && getLink().equals(vacancy.getLink())
                && getCreatedAsString().equals(vacancy.getCreatedAsString()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLink(), getCreatedAsString());
    }

    @Override
    public String toString() {
        return String.format("Vacancy{name = %s, created = %s, link = %s}",
                getName(), getCreatedAsString(), getLink()
        );
    }
}
