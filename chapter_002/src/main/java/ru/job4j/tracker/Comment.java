package ru.job4j.tracker;

import java.util.Objects;
import java.util.Optional;

/**
 * Comment
 *
 * @author Alexandr Moralev (moralev.alexandr@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class Comment {
    private final Integer commentId;
    private final String comment;
    private final Integer itemId;
    private final Long created;

    private Comment(Builder builder) {
        this.commentId = builder.commentId;
        this.comment = builder.comment;
        this.itemId = builder.itemId;
        this.created = builder.created;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Long getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        Comment comment1 = (Comment) o;
        return Objects.equals(getCreated(), comment1.getCreated())
                && Objects.equals(getCommentId(), comment1.getCommentId())
                && Objects.equals(getComment(), comment1.getComment())
                && Objects.equals(getItemId(), comment1.getItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getComment(), getItemId(), getCreated());
    }

    public static Builder newBuilder() {
        return new Comment.Builder();
    }

    public static class Builder {
        private Integer commentId;
        private String comment;
        private Integer itemId;
        private Long created;

        public Builder of(Comment comment) {
            Optional.ofNullable(comment.getCommentId()).ifPresent(id -> this.commentId = id);
            Optional.ofNullable(comment.getComment()).ifPresent(c -> this.comment = c);
            Optional.ofNullable(comment.getCreated()).ifPresent(t -> this.created = t);
            return this;
        }

        public Builder setCommentId(Integer commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setItemId(Integer itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder setCreated(Long created) {
            this.created = created;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }


}
