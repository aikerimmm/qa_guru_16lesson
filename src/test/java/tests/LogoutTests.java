package tests;

import api.ApiClient;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.LogoutBodyModel;
import org.junit.jupiter.api.Test;

public class LogoutTests extends TestBase {

    ApiClient api = new ApiClient();

    String username = "qaguru";
    String password = "qaguru123";

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(loginResponse.refresh());
        api.auth.logout(logoutData);
    }

    @Test
    public void logoutWithInvalidTokenTest() {
        LogoutBodyModel logoutData = new LogoutBodyModel("abcvgd7872");
        api.auth.logoutWithInvalidToken(logoutData);
    }
}