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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Authors API")
@Feature("Create author")
public class CreateAuthorTests extends BaseTest {

    @Test(description = "POST /Authors with a valid payload returns 200 and echoes the author")
    @Story("Create author - happy path")
    @Severity(SeverityLevel.CRITICAL)
    public void createAuthor_validPayload_isAccepted() {
        Author payload = AuthorDataFactory.validAuthor();

        Response response = authorApi.createAuthor(payload);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 when creating an author");
        Author created = response.as(Author.class);
        assertNotNull(created.getId(), "Created author must return an id");
        assertEquals(created.getFirstName(), payload.getFirstName(), "First name should be echoed");
        assertEquals(created.getLastName(), payload.getLastName(), "Last name should be echoed");
    }

    @Test(description = "POST /Authors tolerates an empty JSON body without a 5xx")
    @Story("Create author - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void createAuthor_emptyBody_doesNotServerError() {
        Response response = authorApi.createAuthor(new Author());
        assertTrue(response.statusCode() < 500,
                "An empty body must not cause a 5xx, got " + response.statusCode());
    }

    @Test(description = "POST /Authors with malformed JSON returns a 4xx client error")
    @Story("Create author - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void createAuthor_malformedJson_returnsClientError() {
        Response response = authorApi.request()
                .body("{ broken json ")
                .post("/Authors");
        assertTrue(response.statusCode() >= 400 && response.statusCode() < 500,
                "Malformed JSON should yield a 4xx, got " + response.statusCode());
    }
}
