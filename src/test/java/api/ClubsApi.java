package api;

import models.club.ClubModel;
import models.club.CreateClubBodyModel;
import models.club.GetClubsResponseModel;
import models.club.PatchClubBodyModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.BaseSpec.baseRequestSpec;
import static specs.club.ClubSpec.*;

public class ClubsApi {

    public GetClubsResponseModel getAllClubs() {
        return step("Get list of all clubs", () ->
                given(baseRequestSpec)
                        .when()
                        .get("/clubs/")
                        .then()
                        .spec(getClubsResponseSpec)
                        .extract().as(GetClubsResponseModel.class));
    }

    public ClubModel getClubById(int clubId) {
        return step("Get club by id " + clubId, () ->
                given(baseRequestSpec)
                        .when()
                        .get("/clubs/" + clubId + "/")
                        .then()
                        .spec(getClubByIdResponseSpec)
                        .extract().as(ClubModel.class));
    }

    public ClubModel createClub(CreateClubBodyModel data, String accessToken) {
        return step("Create new club", () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(data)
                        .when()
                        .post("/clubs/")
                        .then()
                        .spec(createClubResponseSpec)
                        .extract().as(ClubModel.class));
    }

    public ClubModel updateClubPut(int clubId, CreateClubBodyModel data, String accessToken) {
        return step("Update club " + clubId + " via PUT", () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(data)
                        .when()
                        .put("/clubs/" + clubId + "/")
                        .then()
                        .spec(updateClubResponseSpec)
                        .extract().as(ClubModel.class));
    }

    public ClubModel updateClubPatch(int clubId, PatchClubBodyModel data, String accessToken) {
        return step("Update club " + clubId + " via PATCH", () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .body(data)
                        .when()
                        .patch("/clubs/" + clubId + "/")
                        .then()
                        .spec(updateClubResponseSpec)
                        .extract().as(ClubModel.class));
    }

    public void deleteClub(int clubId, String accessToken) {
        step("Delete club " + clubId, () ->
                given(baseRequestSpec)
                        .header("Authorization", "Bearer " + accessToken)
                        .when()
                        .delete("/clubs/" + clubId + "/")
                        .then()
                        .spec(deleteClubResponseSpec));
    }

    public void getClubByIdNotFound(int clubId) {
        step("Verify club " + clubId + " no longer exists", () ->
                given(baseRequestSpec)
                        .when()
                        .get("/clubs/" + clubId + "/")
                        .then()
                        .spec(clubNotFoundResponseSpec));
    }
}