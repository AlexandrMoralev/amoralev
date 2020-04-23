package ru.job4j.model;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Ticket
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Ticket {

    private long id;
    private int row;
    private int seat;
    private int price;
    private boolean isOrdered;

    private Ticket(Builder builder) {
        this.id = builder.id;
        this.row = builder.row;
        this.seat = builder.seat;
        this.price = builder.price;
        this.isOrdered = builder.isOrdered;
    }

    public Long getId() {
        return id;
    }

    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public int getPrice() {
        return price;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return getRow() == ticket.getRow()
                && getSeat() == ticket.getSeat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getSeat());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ticket{");
        sb.append("id=").append(id);
        sb.append(", row=").append(row);
        sb.append(", seat=").append(seat);
        sb.append(", price=").append(price);
        sb.append(", isOrdered=").append(isOrdered);
        sb.append('}');
        return sb.toString();
    }

    public static Builder newBuilder() {
        return new Ticket.Builder();
    }

    public static class Builder {

        private Long id;
        private int row;
        private int seat;
        private int price;
        private boolean isOrdered;

        public Builder of(Ticket user) {
            ofNullable(user.getId()).ifPresent(v -> this.id = v);
            Optional.of(user.getRow()).ifPresent(v -> this.row = v);
            Optional.of(user.getSeat()).ifPresent(v -> this.seat = v);
            Optional.of(user.getPrice()).ifPresent(v -> this.price = v);
            Optional.of(user.isOrdered()).ifPresent(v -> this.isOrdered = v);
            return this;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setRow(int row) {
            this.row = row;
            return this;
        }

        public Builder setSeat(int seat) {
            this.seat = seat;
            return this;
        }

        public Builder setPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder setIsOrdered(boolean isOrdered) {
            this.isOrdered = isOrdered;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }

    }

}
