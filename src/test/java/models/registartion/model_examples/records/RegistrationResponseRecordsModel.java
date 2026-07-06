package models.registartion.model_examples.records;

public record RegistrationResponseRecordsModel(
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}