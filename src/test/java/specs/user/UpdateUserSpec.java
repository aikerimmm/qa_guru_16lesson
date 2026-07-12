package specs.user;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;

public class UpdateUserSpec {
    public static RequestSpecification updateUserRequestSpec = with()
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");

    public static ResponseSpecification successfulUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();


    public static ResponseSpecification unauthorizedUpdateUserResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .build();

}

