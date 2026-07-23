package models.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GetClubsResponseModel(
        Integer count,
        String next,
        String previous,
        List<ClubModel> results
) {}