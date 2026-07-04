package tests;

import io.restassured.http.ContentType;
import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {
    @Test
    public void successfulRegistrationTest_bad_practice() {
        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

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
    public void successfulRegistrationTest() {
        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

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
    }

    @Test
    public void successfulRegistrationTest_with_pojo() {
        Faker faker = new Faker();
        String username = faker.name().username() + faker.number().digits(5);
        String password = faker.name().firstName();

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
    public void existingUser400Test() {
        Faker faker = new Faker();
        String username = faker.name().firstName();
        String password = faker.name().firstName();

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
        Faker faker = new Faker();
        String username = faker.name().fullName();
        String password = faker.name().firstName();

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
        Faker faker = new Faker();
        String username = faker.name().fullName();
        String password = faker.name().firstName();

        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";


        given()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .statusCode(415);
    }

//    @Test
//    public void negative500RegistrationTest() {
//        Faker faker = new Faker();
//        String username = faker.name().fullName();
//        String password = faker.name().firstName();
//
//        String data = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
//
//
//        given()
//                .body(data)
//                .when()
//                .post("https://book-club.qa.guru/api/v1/users/register/")
//                .then()
//                .statusCode(201)
//                .body("username", is(username));
//    }
}