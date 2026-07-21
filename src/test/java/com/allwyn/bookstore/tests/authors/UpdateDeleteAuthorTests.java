package com.allwyn.bookstore.tests.authors;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.data.AuthorDataFactory;
import com.allwyn.bookstore.models.Author;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Epic("Authors API")
@Feature("Update and delete author")
public class UpdateDeleteAuthorTests extends BaseTest {

    @Test(description = "PUT /Authors/{id} updates an existing author and echoes new values")
    @Story("Update author - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void updateAuthor_existing_updatesValues() {
        int id = 1;
        Author updated = AuthorDataFactory.validAuthor();
        updated.setId(id);
        updated.setFirstName("Updated");

        Response response = authorApi.updateAuthor(id, updated);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when updating an author");
        Author body = response.as(Author.class);
        assertEquals(body.getFirstName(), "Updated", "First name should reflect the update");
    }

    @Test(description = "PUT /Authors/{id} with a non-numeric id returns 400")
    @Story("Update author - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void updateAuthor_nonNumericId_returns400() {
        Response response = authorApi.updateAuthor("abc", AuthorDataFactory.validAuthor());
        assertEquals(response.statusCode(), 400, "Expected HTTP 400 for a non-numeric id");
    }

    @Test(description = "DELETE /Authors/{id} for an existing author returns 200")
    @Story("Delete author - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteAuthor_existing_returns200() {
        Response response = authorApi.deleteAuthor(1);
        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when deleting an author");
    }

    @Test(description = "DELETE /Authors/{id} with a non-numeric id returns 400")
    @Story("Delete author - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void deleteAuthor_nonNumericId_returns400() {
        Response response = authorApi.deleteAuthor("xyz");
        assertEquals(response.statusCode(), 400, "Expected HTTP 400 for a non-numeric id");
    }

    @Test(description = "DELETE /Authors/{id} for a non-existent id does not server-error")
    @Story("Delete author - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void deleteAuthor_nonExistentId_isHandled() {
        Response response = authorApi.deleteAuthor(777_777);
        assertTrue(response.statusCode() < 500,
                "A non-existent id must not cause a 5xx, got " + response.statusCode());
    }
}
