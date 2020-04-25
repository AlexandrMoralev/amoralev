package ru.job4j.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Account
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Account {

    private Long id;
    private String fio;
    private String phone;

    private Account() {}

    private Account(Builder builder) {
        this.id = builder.id;
        this.fio = builder.fio;
        this.phone = builder.phone;
    }

    public Long getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(getFio(), account.getFio())
                && Objects.equals(getPhone(), account.getPhone());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFio(), getPhone());
    }

    public static Builder newBuilder() {
        return new Account.Builder();
    }

    public static class Builder {

        private Long id;
        private String fio;
        private String phone;

        public Builder of(Account account) {
            ofNullable(account.getId()).ifPresent(v -> this.id = v);
            Optional.of(account.getFio()).ifPresent(v -> this.fio = v);
            Optional.of(account.getPhone()).ifPresent(v -> this.phone = v);
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setFio(String fio) {
            this.fio = fio;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Account build() {
            return new Account(this);
        }
    }
}
