package com.allwyn.bookstore.tests.books;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.data.BookDataFactory;
import com.allwyn.bookstore.models.Book;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Books API")
@Feature("Create book")
public class CreateBookTests extends BaseTest {

    @Test(description = "POST /Books with a valid payload returns 200 and echoes the book")
    @Story("Create book - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void createBook_validPayload_isAccepted() {
        Book payload = BookDataFactory.validBook();

        Response response = bookApi.createBook(payload);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when creating a book");
        Book created = response.as(Book.class);
        assertNotNull(created.getId(), "Created book must return an id");
        assertEquals(created.getTitle(), payload.getTitle(), "Title should be echoed back");
        assertEquals(created.getPageCount(), payload.getPageCount(), "Page count should be echoed back");
    }

    @Test(description = "POST /Books with a minimal payload is accepted")
    @Story("Create book - happy path")
    @Severity(SeverityLevel.NORMAL)
    public void createBook_minimalPayload_isAccepted() {
        Book payload = BookDataFactory.minimalBook();

        Response response = bookApi.createBook(payload);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 for a minimal payload");
        assertEquals(response.as(Book.class).getTitle(), payload.getTitle());
    }

    @Test(description = "POST /Books tolerates an empty JSON body")
    @Story("Create book - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void createBook_emptyBody_doesNotServerError() {
        Response response = bookApi.createBook(new Book());

        assertTrue(response.statusCode() < 500,
                "An empty body must not cause a 5xx server error, got " + response.statusCode());
    }

    @Test(description = "POST /Books with malformed JSON returns a 4xx client error")
    @Story("Create book - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void createBook_malformedJson_returnsClientError() {
        Response response = bookApi.request()
                .body("{ this is not valid json ")
                .post("/Books");

        assertTrue(response.statusCode() >= 400 && response.statusCode() < 500,
                "Malformed JSON should yield a 4xx, got " + response.statusCode());
    }

    @Test(description = "POST /Books with an oversized page count is handled gracefully")
    @Story("Create book - boundary values")
    @Severity(SeverityLevel.MINOR)
    public void createBook_boundaryPageCount_isHandled() {
        Book payload = BookDataFactory.validBook();
        payload.setPageCount(Integer.MAX_VALUE);

        Response response = bookApi.createBook(payload);

        assertTrue(response.statusCode() < 500,
                "Boundary page count must not cause a 5xx, got " + response.statusCode());
    }
}
