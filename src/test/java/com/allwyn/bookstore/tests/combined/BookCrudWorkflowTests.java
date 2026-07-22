package com.allwyn.bookstore.tests.combined;

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

/**
 * End-to-end Book lifecycle: <b>create → update → delete</b>, where each step
 * consumes data produced by the previous one.
 *
 * <p>The chain is expressed with TestNG {@code dependsOnMethods}: if an early
 * step fails, the rest are <em>skipped</em> (not failed), which keeps the report
 * honest about the root cause. State flows through instance fields — safe here
 * because TestNG runs all methods of one class on a single thread.
 */
@Epic("Books API")
@Feature("End-to-end workflows")
public class BookCrudWorkflowTests extends BaseTest {

    private Integer createdId;
    private String createdTitle;

    @Test(description = "Step 1 — create a book and capture the returned id")
    @Story("Book lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void createBook() {
        Book payload = BookDataFactory.validBook();

        Response response = bookApi.createBook(payload);

        assertEquals(response.statusCode(), 200, "Create should return HTTP 200");
        Book created = response.as(Book.class);
        createdId = created.getId();
        createdTitle = created.getTitle();
        assertNotNull(createdId, "Created book must expose an id to chain on");
        assertEquals(createdTitle, payload.getTitle(), "Create must echo the title");
    }

    @Test(dependsOnMethods = "createBook",
            description = "Step 2 — update the book created in step 1")
    @Story("Book lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void updateCreatedBook() {
        Book update = BookDataFactory.validBook();
        update.setId(createdId);
        String newTitle = createdTitle + " - edited";
        update.setTitle(newTitle);

        Response response = bookApi.updateBook(createdId, update);

        assertEquals(response.statusCode(), 200, "Update should return HTTP 200");
        assertEquals(response.as(Book.class).getTitle(), newTitle,
                "Update must echo the new title");
    }

    @Test(dependsOnMethods = "updateCreatedBook",
            description = "Step 3 — delete the book, closing the lifecycle")
    @Story("Book lifecycle")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteCreatedBook() {
        Response response = bookApi.deleteBook(createdId);
        assertEquals(response.statusCode(), 200, "Delete should return HTTP 200");
    }
}