package tests;

import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static specs.user.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.user.UpdateUserSpec.unauthorizedUpdateUserResponseSpec;

public class UpdateUserTests extends TestBase {

    String username;
    String password;
    String accessToken;
    Faker faker = new Faker();

    @BeforeEach
    public void registerAndLogin() {
        username = faker.internet().username();
        password = faker.internet().password(8, 16);

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        given(baseRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec);

        LoginBodyModel loginData = new LoginBodyModel(username, password);

        accessToken = given(baseRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("access");
    }

    @Test
    public void successfulPutUpdateUserTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                faker.internet().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
        UpdateUserResponseModel response = given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(UpdateUserResponseModel.class);

        assertThat(response.username()).isEqualTo(updateData.username());
    }

    @Test
    public void successfulPatchUpdateUserTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                null,
                faker.name().firstName(),
                null,
                null
        );
        UpdateUserResponseModel response = given(baseRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(UpdateUserResponseModel.class);

        assertThat(response.firstName()).isEqualTo(updateData.firstName());
    }

    @Test
    public void updateUserWithoutAuthTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                null,
                faker.name().firstName(),
                null,
                null
        );

        given(baseRequestSpec)
                .body(updateData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(unauthorizedUpdateUserResponseSpec);
    }
}