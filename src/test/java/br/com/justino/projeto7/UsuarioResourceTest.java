package br.com.justino.projeto7;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class UsuarioResourceTest {

    @Test
    public void testHasUserEndpoint() {
        given()
          .when().get("/usuario/1")
          .then()
             .statusCode(is(200))
             //.body(is("{}"))
        ;
    }

}