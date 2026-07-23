package specs.club;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class ClubSpec {

    public static ResponseSpecification getClubsResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("count", greaterThan(0))
            .expectBody("results", notNullValue())
            .build();

    public static ResponseSpecification getClubByIdResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .build();

    public static ResponseSpecification createClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .build();

    public static ResponseSpecification updateClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification deleteClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)   // Django DRF по умолчанию возвращает 204 No Content
            .build();

    public static ResponseSpecification clubNotFoundResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(404)
            .build();
}