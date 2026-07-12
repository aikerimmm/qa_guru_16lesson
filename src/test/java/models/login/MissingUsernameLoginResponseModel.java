package models.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MissingUsernameLoginResponseModel(
        List<String> username
) {}