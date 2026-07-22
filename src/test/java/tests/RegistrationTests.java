package tests;

import models.registration.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        Faker faker = new Faker();
        username = faker.name().username();
        password = faker.name().firstName();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = given(baseRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.id()).isGreaterThan(0);
    }

    @Test
    public void registrationWithoutPasswordTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, null);

        MissingPasswordRegistrationResponseModel errorResponse = given(baseRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(missingPasswordRegistrationResponseSpec)
                .extract()
                .as(MissingPasswordRegistrationResponseModel.class);

        assertThat(errorResponse.password().getFirst())
                .isEqualTo("This field may not be null.");
    }

    @Test
    public void registrationWithoutUsernameTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(null, password);

        MissingUsernameRegistrationResponseModel errorResponse = given(baseRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(missingUsernameRegistrationResponseSpec)
                .extract()
                .as(MissingUsernameRegistrationResponseModel.class);

        assertThat(errorResponse.username().getFirst())
                .isEqualTo("This field may not be null.");
    }

    @Test
    public void registrationWithEmptyBodyTest() {
        EmptyBodyRegistrationResponseModel errorResponse = given(baseRequestSpec)
                .body("{}")
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyBodyRegistrationResponseSpec)
                .extract()
                .as(EmptyBodyRegistrationResponseModel.class);

        assertThat(errorResponse.username().getFirst()).isEqualTo("This field is required.");
        assertThat(errorResponse.password().getFirst()).isEqualTo("This field is required.");
    }

    @Test
    public void registrationWithBlankPasswordTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, "");

        MissingPasswordRegistrationResponseModel errorResponse = given(baseRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(missingPasswordRegistrationResponseSpec)
                .extract()
                .as(MissingPasswordRegistrationResponseModel.class);

        assertThat(errorResponse.password().getFirst())
                .isEqualTo("This field may not be blank.");
    }
    @Test
    public void registrationWithUnsupportedMediaTypeTest() {
        given(textPlainRegistrationRequestSpec)
                .body("username=" + username + "&password=" + password)
                .when()
                .post("/users/register/")
                .then()
                .spec(unsupportedMediaTypeResponseSpec);
    }
}