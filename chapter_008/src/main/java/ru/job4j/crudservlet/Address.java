package ru.job4j.crudservlet;

import java.util.Objects;

import static java.util.Optional.ofNullable;

public class Address {
    private final Integer id;
    private final String country;
    private final String city;

    private Address(Integer id,
                    String country,
                    String city
    ) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    private Address(Builder builder) {
        this.id = builder.id;
        this.country = builder.country;
        this.city = builder.city;
    }

    public Integer getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(getCountry(), address.getCountry())
                && Objects.equals(getCity(), address.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCity());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("id='").append(id).append('\'');
        sb.append("country='").append(country).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new Address.Builder();
    }

    public static class Builder {
        public Address build() {
            return new Address(this);
        }

        private Integer id;
        private String country;
        private String city;

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder of(Address address) {
            ofNullable(address.getId()).ifPresent(v -> this.id = v);
            ofNullable(address.getCountry()).ifPresent(v -> this.country = v);
            ofNullable(address.getCity()).ifPresent(v -> this.city = v);
            return this;
        }
    }
}
