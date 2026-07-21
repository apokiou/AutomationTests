package com.allwyn.bookstore.tests.books;

import com.allwyn.bookstore.base.BaseTest;
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
@Feature("Delete book")
public class DeleteBookTests extends BaseTest {

    @Test(description = "DELETE /Books/{id} for an existing book returns 200")
    @Story("Delete book - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBook_existing_returns200() {
        Response response = bookApi.deleteBook(1);
        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when deleting an existing book");
    }

    @Test(description = "DELETE /Books/{id} with a non-numeric id returns 400")
    @Story("Delete book - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void deleteBook_nonNumericId_returns400() {
        Response response = bookApi.deleteBook("xyz");
        assertEquals(response.statusCode(), 400, "Expected HTTP 400 for a non-numeric id");
    }

    @Test(description = "DELETE /Books/{id} for a non-existent id does not server-error")
    @Story("Delete book - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void deleteBook_nonExistentId_isHandled() {
        Response response = bookApi.deleteBook(999_999);
        assertTrue(response.statusCode() < 500,
                "A non-existent id must not cause a 5xx, got " + response.statusCode());
    }
}
