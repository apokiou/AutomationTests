package com.allwyn.bookstore.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Author resource of the FakeRestAPI.
 * {@code idBook} links the author to a book id.
 *
 * <p>Boilerplate (getters/setters/equals/hashCode/toString/builder) is generated
 * by Lombok.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Author {

    private Integer id;
    private Integer idBook;
    private String firstName;
    private String lastName;
}