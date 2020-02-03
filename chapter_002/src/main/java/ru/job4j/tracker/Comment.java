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
    private final String commentId;
    private final String comment;
    private final String itemId;

    private Comment(Builder builder) {
        this.commentId = builder.commentId;
        this.comment = builder.comment;
        this.itemId = builder.itemId;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getComment() {
        return comment;
    }

    public String getItemId() {
        return itemId;
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
        return Objects.equals(getCommentId(), comment1.getCommentId())
                && Objects.equals(getComment(), comment1.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommentId(), getComment());
    }

    @Override
    public String toString() {
        return "Comment{"
                + "commentId='" + commentId + '\''
                + ", comment='" + comment + '\''
                + ", itemId='" + itemId + '\''
                + '}';
    }

    public static Builder newBuilder() {
        return new Comment.Builder();
    }

    public static class Builder {
        private String commentId;
        private String comment;
        private String itemId;

        public Builder of(Comment comment) {
            Optional.ofNullable(comment.getCommentId()).ifPresent(id -> this.commentId = id);
            Optional.ofNullable(comment.getComment()).ifPresent(c -> this.comment = c);
            return this;
        }

        public Builder setCommentId(String commentId) {
            this.commentId = commentId;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setItemId(String itemId) {
            this.itemId = itemId;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }


}
