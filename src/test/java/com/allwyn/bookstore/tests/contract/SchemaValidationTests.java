package com.allwyn.bookstore.tests.contract;

import com.allwyn.bookstore.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Contract tests: assert that responses honour their JSON <b>schema</b>
 * (field presence and types), not merely the status code. This catches
 * breaking API changes — a renamed or retyped field — that value-level
 * assertions would silently miss.
 */
@Epic("Contract testing")
@Feature("JSON schema validation")
public class SchemaValidationTests extends BaseTest {

    @Test(description = "GET /Books conforms to the books-list schema")
    @Story("Books contract")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllBooks_matchesSchema() {
        bookApi.getAllBooks().then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/books-list-schema.json"));
    }

    @Test(description = "GET /Books/{id} conforms to the book schema")
    @Story("Books contract")
    @Severity(SeverityLevel.CRITICAL)
    public void getBookById_matchesSchema() {
        bookApi.getBookById(1).then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/book-schema.json"));
    }

    @Test(description = "GET /Authors conforms to the authors-list schema")
    @Story("Authors contract")
    @Severity(SeverityLevel.NORMAL)
    public void getAllAuthors_matchesSchema() {
        authorApi.getAllAuthors().then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/authors-list-schema.json"));
    }

    @Test(description = "GET /Authors/{id} conforms to the author schema")
    @Story("Authors contract")
    @Severity(SeverityLevel.NORMAL)
    public void getAuthorById_matchesSchema() {
        authorApi.getAuthorById(1).then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/author-schema.json"));
    }
}
