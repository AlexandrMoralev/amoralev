package ru.job4j.exam.jobparser;

import org.joda.time.LocalDateTime;

import java.util.Objects;

import static java.util.Optional.ofNullable;

/**
 * Vacancy
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Vacancy {
    private final String id;
    private final String name;
    private final String description;
    private final String link;
    private final LocalDateTime created;

    private Vacancy(String id,
                    String name,
                    String description,
                    String link,
                    LocalDateTime created
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
        this.created = created;
    }

    private Vacancy(Builder builder) {
        this(builder.id, builder.name, builder.description, builder.link, builder.created);
    }

    public String getId() {
        return this.id;
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

    public LocalDateTime getCreated() {
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
        return Objects.equals(getName(), vacancy.getName())
                && Objects.equals(getLink(), vacancy.getLink())
                && Objects.equals(getCreated(), vacancy.getCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getLink(), getCreated());
    }

    @Override
    public String toString() {
        return "Vacancy{"
                + "name='" + name + '\''
                + ", description='" + description + '\''
                + ", link='" + link + '\''
                + ", created=" + created
                + '}';
    }

    public static Builder newBuilder() {
        return new Vacancy.Builder();
    }

    public static class Builder {
        private String id;
        private String name;
        private String description;
        private String link;
        private LocalDateTime created;

        public Builder of(Vacancy item) {
            ofNullable(item.getId()).ifPresent(v -> this.id = v);
            ofNullable(item.getName()).ifPresent(v -> this.name = v);
            ofNullable(item.getDescription()).ifPresent(v -> this.description = v);
            ofNullable(item.getLink()).ifPresent(v -> this.link = v);
            ofNullable(item.getCreated()).ifPresent(v -> this.created = v);
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLink(String link) {
            this.link = link;
            return this;
        }

        public Builder setCreated(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public Vacancy build() {
            return new Vacancy(this);
        }
    }
}
