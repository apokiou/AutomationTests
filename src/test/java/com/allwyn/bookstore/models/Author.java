package com.allwyn.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Represents an Author resource of the FakeRestAPI.
 * {@code idBook} links the author to a book id.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {

    private Integer id;
    private Integer idBook;
    private String firstName;
    private String lastName;

    public Author() {
        // Required by Jackson.
    }

    private Author(Builder builder) {
        this.id = builder.id;
        this.idBook = builder.idBook;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
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

    public Integer getIdBook() {
        return idBook;
    }

    public void setIdBook(Integer idBook) {
        this.idBook = idBook;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author author)) {
            return false;
        }
        return Objects.equals(id, author.id)
                && Objects.equals(idBook, author.idBook)
                && Objects.equals(firstName, author.firstName)
                && Objects.equals(lastName, author.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idBook, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Author{id=%d, idBook=%d, firstName='%s', lastName='%s'}"
                .formatted(id, idBook, firstName, lastName);
    }

    public static final class Builder {
        private Integer id;
        private Integer idBook;
        private String firstName;
        private String lastName;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder idBook(Integer idBook) {
            this.idBook = idBook;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Author build() {
            return new Author(this);
        }
    }
}
