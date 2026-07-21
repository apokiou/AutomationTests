package com.allwyn.bookstore.clients;

import com.allwyn.bookstore.config.Endpoints;
import com.allwyn.bookstore.models.Author;
import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Thin, typed wrapper around the {@code /Authors} endpoints.
 */
public class AuthorApiClient extends BaseApiClient {

    @Step("GET all authors")
    public Response getAllAuthors() {
        return request()
                .when()
                .get(Endpoints.AUTHORS);
    }

    @Step("GET author by id: {id}")
    public Response getAuthorById(Object id) {
        return request()
                .pathParam("id", id)
                .when()
                .get(Endpoints.AUTHOR_BY_ID);
    }

    @Step("POST new author")
    public Response createAuthor(Author author) {
        return request()
                .body(author)
                .when()
                .post(Endpoints.AUTHORS);
    }

    @Step("PUT author id: {id}")
    public Response updateAuthor(Object id, Author author) {
        return request()
                .pathParam("id", id)
                .body(author)
                .when()
                .put(Endpoints.AUTHOR_BY_ID);
    }

    @Step("DELETE author id: {id}")
    public Response deleteAuthor(Object id) {
        return request()
                .pathParam("id", id)
                .when()
                .delete(Endpoints.AUTHOR_BY_ID);
    }
}
