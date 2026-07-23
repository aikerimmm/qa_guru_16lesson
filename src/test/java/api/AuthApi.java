package api;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import models.login.MissingPasswordLoginResponseModel;
import models.login.MissingUsernameLoginResponseModel;
import models.logout.LogoutBodyModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApi {

    public SuccessfulLoginResponseModel login(LoginBodyModel data) {
        return step("Login with valid credentials", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().as(SuccessfulLoginResponseModel.class));
    }

    public WrongCredentialsLoginResponseModel loginWithWrongCredentials(LoginBodyModel data) {
        return step("Login with wrong credentials", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(wrongCredentialsLoginResponseSpec)
                        .extract().as(WrongCredentialsLoginResponseModel.class));
    }

    public MissingPasswordLoginResponseModel loginWithoutPassword(LoginBodyModel data) {
        return step("Login without password", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(missingPasswordLoginResponseSpec)
                        .extract().as(MissingPasswordLoginResponseModel.class));
    }

    public MissingUsernameLoginResponseModel loginWithoutUsername(LoginBodyModel data) {
        return step("Login without username", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(missingUsernameLoginResponseSpec)
                        .extract().as(MissingUsernameLoginResponseModel.class));
    }

    public void logout(LogoutBodyModel data) {
        step("Logout with refresh token", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(successfulLogoutResponseSpec));
    }

    public void logoutWithInvalidToken(LogoutBodyModel data) {
        step("Logout with invalid token", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(invalidTokenLogoutResponseSpec));
    }
}