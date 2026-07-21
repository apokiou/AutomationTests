package com.allwyn.bookstore.clients;

import com.allwyn.bookstore.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Base class for all API clients.
 *
 * <p>Owns the single, shared {@link RequestSpecification}: base URI, base path,
 * JSON content type, connection/socket timeouts and the Allure request/response
 * logging filter. Concrete clients (Books, Authors) only describe <em>what</em>
 * call they make, never <em>how</em> to build a request — this is the
 * single-responsibility split that keeps the endpoint classes tiny.
 */
public abstract class BaseApiClient {

    private final RequestSpecification requestSpec;

    protected BaseApiClient() {
        int timeout = ConfigManager.timeoutMs();
        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", timeout)
                        .setParam("http.socket.timeout", timeout));

        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUri())
                .setBasePath(ConfigManager.basePath())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(config)
                .addFilter(new AllureRestAssured())
                .build();
    }

    /**
     * @return a fresh request pre-configured with the shared specification.
     * Public so negative tests can send raw/malformed payloads without a model.
     */
    public RequestSpecification request() {
        return given().spec(requestSpec);
    }
}
