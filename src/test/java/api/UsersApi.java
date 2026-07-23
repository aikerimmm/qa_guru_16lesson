package api;

import io.restassured.specification.RequestSpecification;
import models.registration.*;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserResponseModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.registration.RegistrationSpec.*;
import static specs.user.UpdateUserSpec.*;

public class UsersApi {

    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel data) {
        return step("Register new user", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationResponseSpec)
                        .extract().as(SuccessfulRegistrationResponseModel.class));
    }

    public MissingPasswordRegistrationResponseModel registerWithoutPassword(RegistrationBodyModel data) {
        return step("Register without password", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(missingPasswordRegistrationResponseSpec)
                        .extract().as(MissingPasswordRegistrationResponseModel.class));
    }

    public MissingUsernameRegistrationResponseModel registerWithoutUsername(RegistrationBodyModel data) {
        return step("Register without username", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(missingUsernameRegistrationResponseSpec)
                        .extract().as(MissingUsernameRegistrationResponseModel.class));
    }

    public EmptyBodyRegistrationResponseModel registerWithEmptyBody() {
        return step("Register with empty body", () ->
                given(baseRequestSpec)
                        .body("{}")
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(emptyBodyRegistrationResponseSpec)
                        .extract().as(EmptyBodyRegistrationResponseModel.class));
    }

    public void registerWithUnsupportedMediaType(String rawBody) {
        step("Register with unsupported media type (text/plain)", () ->
                given(textPlainRegistrationRequestSpec)
                        .body(rawBody)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(unsupportedMediaTypeResponseSpec));
    }

    // ==================== UPDATE USER ====================

    public UpdateUserResponseModel updateUserPut(UpdateUserBodyModel data, String accessToken) {
        return step("Update user via PUT (full replace)", () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(data)
                        .when()
                        .put("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract().as(UpdateUserResponseModel.class));
    }

    public UpdateUserResponseModel updateUserPatch(UpdateUserBodyModel data, String accessToken) {
        return step("Update user via PATCH (partial update)", () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(data)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(successfulUpdateUserResponseSpec)
                        .extract().as(UpdateUserResponseModel.class));
    }

    public void updateUserWithoutAuth(UpdateUserBodyModel data) {
        step("Update user without auth token", () ->
                given(baseRequestSpec)
                        .body(data)
                        .when()
                        .patch("/users/me/")
                        .then()
                        .spec(unauthorizedUpdateUserResponseSpec));
    }
}