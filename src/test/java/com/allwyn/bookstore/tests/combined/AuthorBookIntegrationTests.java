package com.allwyn.bookstore.tests.combined;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.data.AuthorDataFactory;
import com.allwyn.bookstore.models.Author;
import com.allwyn.bookstore.models.Book;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * Cross-resource workflow that ties the two APIs together: pick a real book,
 * create an author <b>linked</b> to it via {@code idBook}, update the author,
 * then delete it. Verifies the linkage survives each hop.
 */
@Epic("Cross-resource")
@Feature("Author linked to Book")
public class AuthorBookIntegrationTests extends BaseTest {

    private Integer bookId;
    private Integer authorId;

    @Test(description = "Step 1 — read the catalogue and pick a real book id")
    @Story("Author-Book linkage")
    @Severity(SeverityLevel.CRITICAL)
    public void pickExistingBook() {
        List<Book> books = bookApi.getAllBooks().jsonPath().getList(".", Book.class);

        assertFalse(books.isEmpty(), "Need at least one book to link against");
        bookId = books.get(0).getId();
        assertNotNull(bookId, "Chosen book must have an id");
    }

    @Test(dependsOnMethods = "pickExistingBook",
            description = "Step 2 — create an author linked to the chosen book")
    @Story("Author-Book linkage")
    @Severity(SeverityLevel.CRITICAL)
    public void createAuthorLinkedToBook() {
        Author payload = AuthorDataFactory.validAuthor();
        payload.setIdBook(bookId);

        Response response = authorApi.createAuthor(payload);

        assertEquals(response.statusCode(), 200, "Create author should return HTTP 200");
        Author created = response.as(Author.class);
        assertEquals(created.getIdBook(), bookId, "Author must stay linked to the chosen book");
        authorId = created.getId();
        assertNotNull(authorId, "Created author must expose an id");
    }

    @Test(dependsOnMethods = "createAuthorLinkedToBook",
            description = "Step 3 — update the linked author, keeping the linkage")
    @Story("Author-Book linkage")
    @Severity(SeverityLevel.NORMAL)
    public void updateLinkedAuthor() {
        Author update = AuthorDataFactory.validAuthor();
        update.setId(authorId);
        update.setIdBook(bookId);
        update.setFirstName("Linked");

        Response response = authorApi.updateAuthor(authorId, update);

        assertEquals(response.statusCode(), 200, "Update author should return HTTP 200");
        Author body = response.as(Author.class);
        assertEquals(body.getFirstName(), "Linked", "First name should reflect the update");
        assertEquals(body.getIdBook(), bookId, "Linkage to the book must be preserved");
    }

    @Test(dependsOnMethods = "updateLinkedAuthor",
            description = "Step 4 — delete the linked author, closing the flow")
    @Story("Author-Book linkage")
    @Severity(SeverityLevel.NORMAL)
    public void deleteLinkedAuthor() {
        Response response = authorApi.deleteAuthor(authorId);
        assertEquals(response.statusCode(), 200, "Delete author should return HTTP 200");
    }
}
