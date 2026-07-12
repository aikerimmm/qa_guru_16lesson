package models.registartion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegistrationErrorResponseModel(
        List<String> username,
        List<String> password
) {}