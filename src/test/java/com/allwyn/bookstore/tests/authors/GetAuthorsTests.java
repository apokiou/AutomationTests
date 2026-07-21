package com.allwyn.bookstore.tests.authors;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.models.Author;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Epic("Authors API")
@Feature("Read authors")
public class GetAuthorsTests extends BaseTest {

    @Test(description = "GET /Authors returns 200 and a non-empty list")
    @Story("Get all authors")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllAuthors_returnsNonEmptyList() {
        Response response = authorApi.getAllAuthors();

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 for GET all authors");
        List<Author> authors = response.jsonPath().getList(".", Author.class);
        assertFalse(authors.isEmpty(), "Expected the author list to be non-empty");
    }

    @Test(description = "GET /Authors/{id} returns the requested author for a valid id")
    @Story("Get author by id")
    @Severity(SeverityLevel.CRITICAL)
    public void getAuthorById_validId_returnsAuthor() {
        int existingId = 1;

        Response response = authorApi.getAuthorById(existingId);

        assertEquals(response.statusCode(), 200, "Expected HTTP 200 for an existing author");
        Author author = response.as(Author.class);
        assertNotNull(author.getId(), "Author id must not be null");
        assertEquals(author.getId().intValue(), existingId, "Returned id must match requested id");
    }

    @DataProvider(name = "nonExistentIds")
    public Object[][] nonExistentIds() {
        return new Object[][]{{0}, {-5}, {888_888}};
    }

    @Test(dataProvider = "nonExistentIds",
            description = "GET /Authors/{id} returns 404 for ids that do not exist")
    @Story("Get author by id - edge cases")
    @Severity(SeverityLevel.NORMAL)
    public void getAuthorById_nonExistentId_returns404(int id) {
        Response response = authorApi.getAuthorById(id);
        assertEquals(response.statusCode(), 404,
                "Expected HTTP 404 for non-existent author id " + id);
    }

    @Test(description = "GET /Authors/{id} with a non-numeric id returns 400")
    @Story("Get author by id - edge cases")
    @Severity(SeverityLevel.MINOR)
    public void getAuthorById_nonNumericId_returns400() {
        Response response = authorApi.getAuthorById("abc");
        assertEquals(response.statusCode(), 400, "Expected HTTP 400 for a non-numeric id");
    }

    @Test(description = "Every author exposes first and last name")
    @Story("Get all authors")
    @Severity(SeverityLevel.NORMAL)
    public void getAllAuthors_everyItemHasNames() {
        List<Author> authors = authorApi.getAllAuthors().jsonPath().getList(".", Author.class);
        assertTrue(authors.stream().allMatch(a -> a.getFirstName() != null),
                "Every author must have a first name");
    }
}
