package tests;

import api.ApiClient;
import models.club.ClubModel;
import models.club.CreateClubBodyModel;
import models.club.PatchClubBodyModel;
import models.login.LoginBodyModel;
import models.registration.RegistrationBodyModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsCrudTests extends TestBase {

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

    private CreateClubBodyModel generateClubData() {
        return new CreateClubBodyModel(
                "Book " + faker.number().numberBetween(1000, 9999),
                "Author " + faker.number().numberBetween(1000, 9999),
                faker.number().numberBetween(1800, 2024),
                "Description " + faker.lorem().sentence(),
                "https://t.me/user" + faker.number().numberBetween(1000, 9999)
        );
    }

    @Test
    public void createClubTest() {
        CreateClubBodyModel clubData = generateClubData();

        ClubModel createdClub = api.clubs.createClub(clubData, accessToken);

        step("Verify created club fields match input", () -> {
            assertThat(createdClub.id()).isNotNull().isGreaterThan(0);
            assertThat(createdClub.bookTitle()).isEqualTo(clubData.bookTitle());
            assertThat(createdClub.bookAuthors()).isEqualTo(clubData.bookAuthors());
            assertThat(createdClub.publicationYear()).isEqualTo(clubData.publicationYear());
        });
    }

    @Test
    public void readClubByIdTest() {
        CreateClubBodyModel clubData = generateClubData();
        ClubModel created = api.clubs.createClub(clubData, accessToken);

        ClubModel fetched = api.clubs.getClubById(created.id());

        step("Verify fetched club matches created", () -> {
            assertThat(fetched.id()).isEqualTo(created.id());
            assertThat(fetched.bookTitle()).isEqualTo(created.bookTitle());
            assertThat(fetched.bookAuthors()).isEqualTo(created.bookAuthors());
        });
    }

    @Test
    public void updateClubPutTest() {
        ClubModel created = api.clubs.createClub(generateClubData(), accessToken);
        CreateClubBodyModel newData = generateClubData();

        ClubModel updated = api.clubs.updateClubPut(created.id(), newData, accessToken);

        step("Verify all fields were replaced", () -> {
            assertThat(updated.bookTitle()).isEqualTo(newData.bookTitle());
            assertThat(updated.bookAuthors()).isEqualTo(newData.bookAuthors());
            assertThat(updated.publicationYear()).isEqualTo(newData.publicationYear());
        });
    }

    @Test
    public void updateClubPatchTest() {
        ClubModel created = api.clubs.createClub(generateClubData(), accessToken);
        String newTitle = faker.book().title();
        PatchClubBodyModel patchData = new PatchClubBodyModel(
                newTitle, null, null, null, null
        );

        ClubModel updated = api.clubs.updateClubPatch(created.id(), patchData, accessToken);

        step("Verify only bookTitle was updated, other fields preserved", () -> {
            assertThat(updated.bookTitle()).isEqualTo(newTitle);
            assertThat(updated.bookAuthors()).isEqualTo(created.bookAuthors());
        });
    }

    @Test
    public void deleteClubTest() {
        ClubModel created = api.clubs.createClub(generateClubData(), accessToken);

        api.clubs.deleteClub(created.id(), accessToken);

        api.clubs.getClubByIdNotFound(created.id());
    }
}