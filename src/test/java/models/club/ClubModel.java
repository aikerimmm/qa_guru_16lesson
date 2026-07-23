package models.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ClubModel(
        Integer id,
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink,
        Integer owner,
        List<Integer> members,
        List<Object> reviews,
        String created,
        String modified
) {}