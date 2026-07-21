package com.allwyn.bookstore.tests.combined;

import com.allwyn.bookstore.base.BaseTest;
import com.allwyn.bookstore.data.BookDataFactory;
import com.allwyn.bookstore.models.Author;
import com.allwyn.bookstore.models.Book;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * Combined checks that assert agreement <em>between</em> endpoints, rather than
 * exercising a single call. These catch representation drift the isolated tests
 * would miss (e.g. list and by-id returning different data for the same record).
 */
@Epic("Cross-resource")
@Feature("Data consistency")
public class DataConsistencyTests extends BaseTest {

    @Test(description = "A book fetched by id matches its entry in the full list")
    @Story("List vs by-id agreement")
    @Severity(SeverityLevel.NORMAL)
    public void bookByIdMatchesListEntry() {
        List<Book> all = bookApi.getAllBooks().jsonPath().getList(".", Book.class);
        assertFalse(all.isEmpty(), "Catalogue must not be empty");

        Book fromList = all.get(0);
        Book byId = bookApi.getBookById(fromList.getId()).as(Book.class);

        assertEquals(byId.getId(), fromList.getId(), "Ids must agree");
        assertEquals(byId.getTitle(), fromList.getTitle(), "Titles must agree");
        assertEquals(byId.getPageCount(), fromList.getPageCount(), "Page counts must agree");
    }

    @Test(description = "An author fetched by id matches its entry in the full list")
    @Story("List vs by-id agreement")
    @Severity(SeverityLevel.NORMAL)
    public void authorByIdMatchesListEntry() {
        List<Author> all = authorApi.getAllAuthors().jsonPath().getList(".", Author.class);
        assertFalse(all.isEmpty(), "Author list must not be empty");

        Author fromList = all.get(0);
        Author byId = authorApi.getAuthorById(fromList.getId()).as(Author.class);

        assertEquals(byId.getId(), fromList.getId(), "Ids must agree");
        assertEquals(byId.getFirstName(), fromList.getFirstName(), "First names must agree");
    }

    @Test(description = "POST /Books echoes back exactly what was sent (round-trip integrity)")
    @Story("Round-trip integrity")
    @Severity(SeverityLevel.NORMAL)
    public void createBookRoundTripIntegrity() {
        Book payload = BookDataFactory.validBook();

        Book echoed = bookApi.createBook(payload).as(Book.class);

        assertEquals(echoed.getTitle(), payload.getTitle(), "Title must round-trip unchanged");
        assertEquals(echoed.getPageCount(), payload.getPageCount(), "Page count must round-trip");
        assertEquals(echoed.getDescription(), payload.getDescription(), "Description must round-trip");
    }
}
