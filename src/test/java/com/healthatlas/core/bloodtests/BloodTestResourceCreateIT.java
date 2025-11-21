package com.healthatlas.core.bloodtests;

import com.healthatlas.core.bloodtests.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.util.PostgresTestResource;
import com.healthatlas.core.bloodtests.util.TestKeyResource;
import com.healthatlas.core.bloodtests.util.TestTokenFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(TestKeyResource.class)
@QuarkusTestResource(PostgresTestResource.class)
@DisplayName("Blood tests creation and query integration tests")
public class BloodTestResourceCreateIT {

    @Test
    void shouldCreateBloodTest() {
        BloodTestDto request = new BloodTestDto();
        request.timestamp = Instant.now();
        request.confirmed = false;

        var resultHgb = new BloodTestDto.ResultDto();
        resultHgb.analyte = "HGB";
        resultHgb.value = new BigDecimal("13.5");

        var resultRbc = new BloodTestDto.ResultDto();
        resultRbc.analyte = "RBC";
        resultRbc.value = new BigDecimal("4.8");

        request.results = List.of(resultRbc, resultHgb);

        String token = TestTokenFactory.createUserToken(UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/blood-tests")
                .then()
                .statusCode(201)
                .body("results", hasSize(2))
                .body("results.analyte", hasItems("HGB", "RBC"))
                .body("results.value", hasItems(13.5f, 4.8f));
    }

    @Test
    void shouldFailWithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/blood-tests")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldFailWithWrongRole() {
        String token = Jwt.issuer("test-issuer")
                .upn("other@healthatlas.com")
                .claim("user_id", UUID.randomUUID().toString())
                .claim("groups", List.of("guest"))
                .expiresIn(3600)
                .sign(TestKeyResource.PRIVATE_KEY_URI);

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(new BloodTestDto())
                .when()
                .post("/api/v1/blood-tests")
                .then()
                .statusCode(403); // Forbidden
    }

    @Test
    void shouldFailIfNoResultsProvided() {
        BloodTestDto request = new BloodTestDto();
        request.timestamp = Instant.now();
        request.confirmed = false;
        request.results = List.of();

        String token = TestTokenFactory.createUserToken(UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/blood-tests")
                .then()
                .statusCode(400)
                .body("error", equalTo("ResteasyReactiveViolationException"))
                .body("message", equalTo("create.bloodTestDto.results: At least one analyte result is required."))
                .body("timestamp", notNullValue())
                .body("traceId", notNullValue());
    }

    @Test
    void shouldFailIfInvalidAnalyteProvided() {
        BloodTestDto request = new BloodTestDto();
        request.timestamp = Instant.now();
        request.confirmed = false;

        var resultRbc = new BloodTestDto.ResultDto();
        resultRbc.analyte = "INVLD";
        resultRbc.value = new BigDecimal("4.8");

        request.results = List.of(resultRbc);

        String token = TestTokenFactory.createUserToken(UUID.randomUUID());

        given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/blood-tests")
                .then()
                .statusCode(400)
                .body("error", equalTo("IllegalArgumentException"))
                .body("message", equalTo("Unknown analyte: INVLD"))
                .body("timestamp", notNullValue())
                .body("traceId", notNullValue());
    }
}
