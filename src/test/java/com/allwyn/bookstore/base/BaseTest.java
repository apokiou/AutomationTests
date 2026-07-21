package com.allwyn.bookstore.base;

import com.allwyn.bookstore.clients.AuthorApiClient;
import com.allwyn.bookstore.clients.BookApiClient;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import org.testng.annotations.BeforeClass;

/**
 * Common setup shared by every test class.
 *
 * <p>Provides ready-to-use API clients and configures a single, lenient Jackson
 * mapper for (de)serialisation so individual tests never wire this up themselves.
 */
public abstract class BaseTest {

    protected BookApiClient bookApi;
    protected AuthorApiClient authorApi;

    @BeforeClass(alwaysRun = true)
    public void baseSetUp() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        RestAssured.config = RestAssured.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .jackson2ObjectMapperFactory((type, charset) -> mapper));

        bookApi = new BookApiClient();
        authorApi = new AuthorApiClient();
    }
}
