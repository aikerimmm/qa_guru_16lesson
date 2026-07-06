package tests;

import io.restassured.http.ContentType;
import models.registartion.ExistingUserResponseModel;
import models.registartion.RegistrationBodyModel;
import models.registartion.SuccessfulRegistrationResponseModel;
import net.datafaker.Faker;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().username();  // ← без String
        password = faker.name().firstName();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username,password);

   SuccessfulRegistrationResponseModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/registration/successful_registration_response_schema.json"))
                .body("username", notNullValue())
                .body("id", notNullValue())
                .body("remoteAddr", notNullValue())
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

    assertThat(registrationResponse.username()).isEqualTo(username);
    assertThat(registrationResponse.id()).isGreaterThan(0);
    assertThat(registrationResponse.firstName()).isEqualTo("");
    assertThat(registrationResponse.lastName()).isEqualTo("");
    assertThat(registrationResponse.email()).isEqualTo("");

String ipAddrRegexp = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
assertThat(registrationResponse.remoteAddr()).matches(ipAddrRegexp);
    }

    @Test
    public void existingUserWrongRegistrationTest(){
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username,password);

        SuccessfulRegistrationResponseModel firstRegistrationResponseModel = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .body(matchesJsonSchemaInClasspath("schemas/registration/successful_registration_response_schema.json"))
                .body("username", notNullValue())
                .body("id", notNullValue())
                .body("remoteAddr", notNullValue())
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertThat(firstRegistrationResponseModel.username()).isEqualTo(username);

       ExistingUserResponseModel secondRegistrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(registrationData)
                .basePath("/api/v1")
                .when()
                .post("/users/register/")
                .then()
                .log().all()
                .statusCode(400)
                .body(matchesJsonSchemaInClasspath("schemas/registration/existing_user_registration_response_schema.json"))
                .body("username", notNullValue())
                .extract()
                .as(ExistingUserResponseModel.class);

       String expectedError = "A user with that username already exists.";
       String actualError = secondRegistrationResponse.username().getFirst();
       assertThat(actualError).isEqualTo(expectedError);

    }
    }
