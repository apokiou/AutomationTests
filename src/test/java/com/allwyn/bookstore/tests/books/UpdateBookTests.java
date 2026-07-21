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
import static org.testng.Assert.assertTrue;

@Epic("Books API")
@Feature("Update book")
public class UpdateBookTests extends BaseTest {

    @Test(description = "PUT /Books/{id} updates an existing book and echoes new values")
    @Story("Update book - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBook_existing_updatesValues() {
        int id = 1;
        Book updated = BookDataFactory.validBook();
        updated.setId(id);
        updated.setTitle("Updated title");

        Response response = bookApi.updateBook(id, updated);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when updating a book");
        Book body = response.as(Book.class);
        assertEquals(body.getTitle(), "Updated title", "Title should reflect the update");
        assertEquals(body.getId().intValue(), id, "Id should remain the path id");
    }

    @Test(description = "PUT /Books/{id} with a non-numeric id returns 400")
    @Story("Update book - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void updateBook_nonNumericId_returns400() {
        Response response = bookApi.updateBook("abc", BookDataFactory.validBook());
        assertEquals(response.statusCode(), 400, "Expected HTTP 400 for a non-numeric id");
    }

    @Test(description = "PUT /Books/{id} for a very large id does not server-error")
    @Story("Update book - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void updateBook_largeId_isHandled() {
        Response response = bookApi.updateBook(999_999, BookDataFactory.validBook());
        assertTrue(response.statusCode() < 500,
                "A large id must not cause a 5xx, got " + response.statusCode());
    }
}
