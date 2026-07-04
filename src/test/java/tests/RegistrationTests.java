package tests;

import io.restassured.http.ContentType;
import models.lombok.RegistrationBodyLombokModel;
import models.lombok.RegistrationResponseLombokModel;
import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegistrationTests {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().username();  // ← без String
        password = faker.name().firstName();
    }

    @Test
    public void successfulRegistrationTest_bad_practice() {


        //move to model
        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)

                //move to model
                .body("username", is(username))
                .body("id",notNullValue());
    }

    @Test
    public void successfulRegistrationTest_with_pojo() {

        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        RegistrationResponsePojoModel responsePojoModel = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponsePojoModel.class);

        assertEquals(username, responsePojoModel.getUsername());
    }

    @Test
    public void successfulRegistrationTest_with_Lombok() {

        RegistrationBodyLombokModel data = new RegistrationBodyLombokModel();
        data.setUsername(username);
        data.setPassword(password);

        RegistrationResponsePojoModel responseLombokModel = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponseLombokModel.class);

        assertEquals(username, responseLombokModel.getUsername());
    }

    @Test
    public void existingUser400Test() {

        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(username))
                .body("id",notNullValue());

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body("username[0]", containsString("already exists"));
    }

    @Test
    public void invalidUserName400Test() {
        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body("username[0]", notNullValue());
    }

    @Test
    public void unsupportedMediaType415RegistrationTest() {

        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .statusCode(415);
    }

    @Test
    public void negativeRegistration400InvalidJsonTest() {
        given()
                .contentType(ContentType.JSON)
                .body("broken json {{{")
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body("detail", containsString("JSON parse error"));
    }
}