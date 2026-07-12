package tests;

import models.login.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;


public class LoginTests extends TestBase {

    String username = "qaguru";
    String password = "qaguru123";
    String wrongPassword = "wrongPassword123";

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();

        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);

    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, wrongPassword);;
        WrongCredentialsLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);

        String expectedDetailError = "Invalid username or password.";
        String actualDetailError = loginResponse.detail();

        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    public void loginWithoutPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, null);
        MissingPasswordLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(missingPasswordLoginResponseSpec)
                .extract()
                .as(MissingPasswordLoginResponseModel.class);


        assertThat(loginResponse.password().getFirst())
                .isEqualTo("This field may not be null.");
    }


    @Test
    public void loginWithoutUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(null, password);
        MissingUsernameLoginResponseModel loginResponse = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(missingUsernameLoginResponseSpec)
                .extract()
                .as(MissingUsernameLoginResponseModel.class);


        assertThat(loginResponse.username().getFirst())
                .isEqualTo("This field may not be null.");
    }

}
