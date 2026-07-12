package models.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserResponseModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email
) {}