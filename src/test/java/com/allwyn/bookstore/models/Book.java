package com.allwyn.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Represents a Book resource of the FakeRestAPI.
 *
 * <p>Uses a fluent builder so tests read declaratively, e.g.
 * {@code Book.builder().title("X").pageCount(100).build()}.
 * Unknown JSON fields are ignored to keep the model resilient to API additions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {

    private Integer id;
    private String title;
    private String description;
    private Integer pageCount;
    private String excerpt;
    private String publishDate;

    public Book() {
        // Required by Jackson.
    }

    private Book(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.pageCount = builder.pageCount;
        this.excerpt = builder.excerpt;
        this.publishDate = builder.publishDate;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book book)) {
            return false;
        }
        return Objects.equals(id, book.id)
                && Objects.equals(title, book.title)
                && Objects.equals(description, book.description)
                && Objects.equals(pageCount, book.pageCount)
                && Objects.equals(excerpt, book.excerpt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, pageCount, excerpt);
    }

    @Override
    public String toString() {
        return "Book{id=%d, title='%s', pageCount=%d}".formatted(id, title, pageCount);
    }

    public static final class Builder {
        private Integer id;
        private String title;
        private String description;
        private Integer pageCount;
        private String excerpt;
        private String publishDate;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder pageCount(Integer pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder excerpt(String excerpt) {
            this.excerpt = excerpt;
            return this;
        }

        public Builder publishDate(String publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}
