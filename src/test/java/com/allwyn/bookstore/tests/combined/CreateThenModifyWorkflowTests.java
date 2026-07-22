package com.allwyn.bookstore.tests.combined;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.data.AuthorDataFactory;
import com.allwyn.bookstore.data.BookDataFactory;
import com.allwyn.bookstore.models.Author;
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
import static org.testng.Assert.assertNotEquals;

@Epic("Cross-resource")
@Feature("Create book then re-link author")
public class CreateThenModifyWorkflowTests extends BaseTest {

    private Integer firstBookId;
    private Integer secondBookId;
    private Integer authorId;

    @Test(description = "Step 1 — create two distinct books")
    @Story("Re-link author")
    @Severity(SeverityLevel.NORMAL)
    public void createTwoBooks() {
        Book first = bookApi.createBook(BookDataFactory.validBook()).as(Book.class);
        Book second = bookApi.createBook(BookDataFactory.validBook()).as(Book.class);

        firstBookId = first.getId();
        secondBookId = second.getId();
        assertNotNull(firstBookId, "First book must have an id");
        assertNotNull(secondBookId, "Second book must have an id");
        assertNotEquals(firstBookId, secondBookId, "The two books must be distinct");
    }

    @Test(dependsOnMethods = "createTwoBooks",
            description = "Step 2 — create an author linked to the first book")
    @Story("Re-link author")
    @Severity(SeverityLevel.NORMAL)
    public void createAuthorOnFirstBook() {
        Author payload = AuthorDataFactory.validAuthor();
        payload.setIdBook(firstBookId);

        Author created = authorApi.createAuthor(payload).as(Author.class);
        authorId = created.getId();

        assertNotNull(authorId, "Author must have an id");
        assertEquals(created.getIdBook(), firstBookId, "Author should link to the first book");
    }

    @Test(dependsOnMethods = "createAuthorOnFirstBook",
            description = "Step 3 — re-link the author to the second book")
    @Story("Re-link author")
    @Severity(SeverityLevel.NORMAL)
    public void relinkAuthorToSecondBook() {
        Author update = AuthorDataFactory.validAuthor();
        update.setId(authorId);
        update.setIdBook(secondBookId);

        Response response = authorApi.updateAuthor(authorId, update);

        assertEquals(response.statusCode(), 200, "Update should return HTTP 200");
        assertEquals(response.as(Author.class).getIdBook(), secondBookId,
                "Author must now link to the second book");
    }
}