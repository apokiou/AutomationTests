package com.allwyn.bookstore.clients;

import com.allwyn.bookstore.config.Endpoints;
import com.allwyn.bookstore.models.Book;
import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Thin, typed wrapper around the {@code /Books} endpoints.
 * Each method maps to exactly one HTTP call and returns the raw {@link Response}
 * so that assertions live in the test layer, not here.
 */
public class BookApiClient extends BaseApiClient {

    @Step("GET all books")
    public Response getAllBooks() {
        return request()
                .when()
                .get(Endpoints.BOOKS);
    }

    @Step("GET book by id: {id}")
    public Response getBookById(Object id) {
        return request()
                .pathParam("id", id)
                .when()
                .get(Endpoints.BOOK_BY_ID);
    }

    @Step("POST new book")
    public Response createBook(Book book) {
        return request()
                .body(book)
                .when()
                .post(Endpoints.BOOKS);
    }

    @Step("PUT book id: {id}")
    public Response updateBook(Object id, Book book) {
        return request()
                .pathParam("id", id)
                .body(book)
                .when()
                .put(Endpoints.BOOK_BY_ID);
    }

    @Step("DELETE book id: {id}")
    public Response deleteBook(Object id) {
        return request()
                .pathParam("id", id)
                .when()
                .delete(Endpoints.BOOK_BY_ID);
    }
}
