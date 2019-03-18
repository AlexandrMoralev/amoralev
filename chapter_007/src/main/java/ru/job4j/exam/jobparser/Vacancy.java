package ru.job4j.exam.jobparser;

import java.util.Objects;

/**
 * Vacancy
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Vacancy { // implements TargetItem
    private Integer id;
    private final String name;
    private final String description;
    private final String link;
    private final String created;

    public Vacancy(final String name,
                   final String description,
                   final String link,
                   final String created
    ) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.created = created;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
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

    public String getCreated() {
        return created;
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
                && getCreated().equals(vacancy.getCreated()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLink(), getCreated());
    }

    @Override
    public String toString() {
        return String.format("Vacancy{name = %s, created = %s, link = %s}",
                getName(), getCreated(), getLink()
        );
    }
}
