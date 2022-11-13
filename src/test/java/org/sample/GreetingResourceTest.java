package org.sample;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.sample.resources.WireMockExtensions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@QuarkusTestResource(WireMockExtensions.class)
public class GreetingResourceTest {

    @Test
    public void testExtensionsIdEndpoint() {
        given()
          .when().get("/extensions/id/io.quarkus:quarkus-rest-client")
          .then()
             .statusCode(200)
             .body("$.size()", is(1),
                     "[0].id", is("io.quarkus:quarkus-rest-client"),
                     "[0].name", is("REST Client Classic"),
                     "[0].shortName", is("REST Client Classic"),
                     "[0].keywords.size()", Matchers.greaterThan(1),
                     "[0].keywords", hasItem("rest-client"));
    }

}