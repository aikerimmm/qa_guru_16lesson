package tests;

import api.ApiClient;
import models.login.*;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends TestBase {

    ApiClient api = new ApiClient();

    String username = "qaguru";
    String password = "qaguru123";
    String wrongPassword = "wrongPassword123";

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        step("Проверка полученных access и refresh токенов", () -> {
            String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            assertThat(loginResponse.access()).startsWith(expectedTokenPath);
            assertThat(loginResponse.refresh()).startsWith(expectedTokenPath);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, wrongPassword);
        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWithWrongCredentials(loginData);

        step("Проверка сообщения об ошибке", () ->
                assertThat(loginResponse.detail()).isEqualTo("Invalid username or password."));
    }

    @Test
    public void loginWithoutPasswordTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, null);
        MissingPasswordLoginResponseModel loginResponse = api.auth.loginWithoutPassword(loginData);

        step("Проверка сообщения об отсутствии пароля", () ->
                assertThat(loginResponse.password().getFirst())
                        .isEqualTo("This field may not be null."));
    }

    @Test
    public void loginWithoutUsernameTest() {
        LoginBodyModel loginData = new LoginBodyModel(null, password);
        MissingUsernameLoginResponseModel loginResponse = api.auth.loginWithoutUsername(loginData);

        step("Проверка сообщения об отсутствии имени пользователя", () ->
                assertThat(loginResponse.username().getFirst())
                        .isEqualTo("This field may not be null."));
    }
}