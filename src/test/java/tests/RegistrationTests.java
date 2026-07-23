package tests;

import api.ApiClient;
import models.registration.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTests extends TestBase {

    ApiClient api = new ApiClient();
    Faker faker = new Faker();

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        username = faker.name().username();
        password = faker.name().firstName();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel response = api.users.register(data);

        step("Verify registered user data", () -> {
            assertThat(response.username()).isEqualTo(username);
            assertThat(response.id()).isGreaterThan(0);
        });
    }

    @Test
    public void registrationWithoutPasswordTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(username, null);
        MissingPasswordRegistrationResponseModel response = api.users.registerWithoutPassword(data);

        step("Verify missing password error", () ->
                assertThat(response.password().getFirst())
                        .isEqualTo("This field may not be null."));
    }

    @Test
    public void registrationWithoutUsernameTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(null, password);
        MissingUsernameRegistrationResponseModel response = api.users.registerWithoutUsername(data);

        step("Verify missing username error", () ->
                assertThat(response.username().getFirst())
                        .isEqualTo("This field may not be null."));
    }

    @Test
    public void registrationWithEmptyBodyTest() {
        EmptyBodyRegistrationResponseModel response = api.users.registerWithEmptyBody();

        step("Verify empty body errors", () -> {
            assertThat(response.username().getFirst()).isEqualTo("This field is required.");
            assertThat(response.password().getFirst()).isEqualTo("This field is required.");
        });
    }

    @Test
    public void registrationWithBlankPasswordTest() {
        RegistrationBodyModel data = new RegistrationBodyModel(username, "");
        MissingPasswordRegistrationResponseModel response = api.users.registerWithoutPassword(data);

        step("Verify blank password error", () ->
                assertThat(response.password().getFirst())
                        .isEqualTo("This field may not be blank."));
    }

    @Test
    public void registrationWithUnsupportedMediaTypeTest() {
        String body = "username=" + username + "&password=" + password;
        api.users.registerWithUnsupportedMediaType(body);
    }
}