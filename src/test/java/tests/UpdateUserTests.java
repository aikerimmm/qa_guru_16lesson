package tests;

import api.ApiClient;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import models.user.UpdateUserBodyModel;
import models.user.UpdateUserResponseModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {

    ApiClient api = new ApiClient();
    Faker faker = new Faker();

    String username;
    String password;
    String accessToken;

    @BeforeEach
    public void registerAndLogin() {
        username = faker.internet().username();
        password = faker.internet().password(8, 16);

        api.users.register(new RegistrationBodyModel(username, password));
        accessToken = api.auth.login(new LoginBodyModel(username, password)).access();
    }

    @Test
    public void successfulPutUpdateUserTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                faker.internet().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );

        UpdateUserResponseModel response = api.users.updateUserPut(updateData, accessToken);

        step("Verify username was updated", () ->
                assertThat(response.username()).isEqualTo(updateData.username()));
    }

    @Test
    public void successfulPatchUpdateUserTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                null,
                faker.name().firstName(),
                null,
                null
        );

        UpdateUserResponseModel response = api.users.updateUserPatch(updateData, accessToken);

        step("Verify firstName was updated", () ->
                assertThat(response.firstName()).isEqualTo(updateData.firstName()));
    }

    @Test
    public void updateUserWithoutAuthTest() {
        UpdateUserBodyModel updateData = new UpdateUserBodyModel(
                null,
                faker.name().firstName(),
                null,
                null
        );

        api.users.updateUserWithoutAuth(updateData);
    }
}