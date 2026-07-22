package tests;

import models.login.*;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.BaseSpec.baseRequestSpec;
import static specs.login.LoginSpec.*;

public class LoginTests extends TestBase {

    String username = "qaguru";
    String password = "qaguru123";
    String wrongPassword = "wrongPassword123";

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = step("Подготовка тела запроса с валидными данными", () ->
                new LoginBodyModel(username, password));

        SuccessfulLoginResponseModel loginResponse = step("Отправка запроса на авторизацию", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().as(SuccessfulLoginResponseModel.class));

        step("Проверка полученных access и refresh токенов", () -> {
            String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            assertThat(loginResponse.access()).startsWith(expectedTokenPath);
            assertThat(loginResponse.refresh()).startsWith(expectedTokenPath);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = step("Подготовка тела запроса с неверным паролем", () ->
                new LoginBodyModel(username, wrongPassword));

        WrongCredentialsLoginResponseModel loginResponse = step("Отправка запроса с неверными данными", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(wrongCredentialsLoginResponseSpec)
                        .extract().as(WrongCredentialsLoginResponseModel.class));

        step("Проверка сообщения об ошибке в ответе", () ->
                assertThat(loginResponse.detail()).isEqualTo("Invalid username or password."));
    }

    @Test
    public void loginWithoutPasswordTest() {
        LoginBodyModel loginData = step("Подготовка тела запроса без пароля", () ->
                new LoginBodyModel(username, null));

        MissingPasswordLoginResponseModel loginResponse = step("Отправка запроса без пароля", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(missingPasswordLoginResponseSpec)
                        .extract().as(MissingPasswordLoginResponseModel.class));

        step("Проверка сообщения об отсутствии пароля", () ->
                assertThat(loginResponse.password().getFirst())
                        .isEqualTo("This field may not be null."));
    }

    @Test
    public void loginWithoutUsernameTest() {
        LoginBodyModel loginData = step("Подготовка тела запроса без имени пользователя", () ->
                new LoginBodyModel(null, password));

        MissingUsernameLoginResponseModel loginResponse = step("Отправка запроса без имени пользователя", () ->
                given(baseRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(missingUsernameLoginResponseSpec)
                        .extract().as(MissingUsernameLoginResponseModel.class));

        step("Проверка сообщения об отсутствии имени пользователя", () ->
                assertThat(loginResponse.username().getFirst())
                        .isEqualTo("This field may not be null."));
    }
}