package com.allwyn.bookstore.tests.books;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.models.Book;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Books API")
@Feature("Read books")
public class GetBooksTests extends BaseTest {

    @Test(description = "GET /Books returns 200 and a non-empty list")
    @Story("Get all books")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllBooks_returnsNonEmptyList() {
        Response response = bookApi.getAllBooks();

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 for GET all books");
        List<Book> books = response.jsonPath().getList(".", Book.class);
        assertFalse(books.isEmpty(), "Expected the book list to be non-empty");
        response.then().body("size()", greaterThan(0));
    }

    @Test(description = "GET /Books/{id} returns the requested book for a valid id")
    @Story("Get book by id")
    @Severity(SeverityLevel.CRITICAL)
    public void getBookById_validId_returnsBook() {
        int existingId = 1;

        Response response = bookApi.getBookById(existingId);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 for an existing book");
        Book book = response.as(Book.class);
        assertNotNull(book.getId(), "Book id must not be null");
        assertEquals(book.getId().intValue(), existingId, "Returned id must match the requested id");
        assertNotNull(book.getTitle(), "Book title must not be null");
    }

    @Test(description = "Every book in the list exposes an id and a title")
    @Story("Get all books")
    @Severity(SeverityLevel.NORMAL)
    public void getAllBooks_everyItemHasIdAndTitle() {
        List<Book> books = bookApi.getAllBooks().jsonPath().getList(".", Book.class);

        assertTrue(books.stream().allMatch(b -> b.getId() != null),
                "Every book must have an id");
        assertTrue(books.stream().allMatch(b -> b.getTitle() != null && !b.getTitle().isBlank()),
                "Every book must have a non-blank title");
    }

    @DataProvider(name = "nonExistentIds")
    public Object[][] nonExistentIds() {
        return new Object[][]{{0}, {-1}, {999_999}};
    }

    @Test(dataProvider = "nonExistentIds",
            description = "GET /Books/{id} returns 404 for ids that do not exist")
    @Story("Get book by id - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void getBookById_nonExistentId_returns404(int id) {
        Response response = bookApi.getBookById(id);
        assertEquals(response.statusCode(), 404,
                "Expected HTTP 404 for non-existent book id " + id);
    }

    @Test(description = "GET /Books/{id} returns 400 for a non-numeric id")
    @Story("Get book by id - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void getBookById_nonNumericId_returns400() {
        Response response = bookApi.getBookById("not-a-number");
        assertEquals(response.statusCode(), 400,
                "Expected HTTP 400 for a malformed (non-numeric) id");
    }

    @Test(description = "GET /Books responds within an acceptable time budget")
    @Story("Get all books - performance")
    @Severity(SeverityLevel.MINOR)
    public void getAllBooks_respondsWithinBudget() {
        Response response = bookApi.getAllBooks();
        long millis = response.time();
        assertTrue(millis < 5_000, "Response took too long: " + millis + "ms");
    }
}
