package com.healthatlas.core.bloodtests;

import com.healthatlas.core.bloodtests.util.PostgresTestResource;
import com.healthatlas.core.bloodtests.util.TestDataFactory;
import com.healthatlas.core.bloodtests.util.TestKeyResource;
import com.healthatlas.core.bloodtests.util.TestTokenFactory;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(TestKeyResource.class)
@QuarkusTestResource(PostgresTestResource.class)
@DisplayName("Blood tests - GET /mine integrated tests")
public class BloodTestResourceQueryIT {

    @Inject
    TestDataFactory factory;

    @Test
    @DisplayName("Should return blood tests ordered by timestamp descending")
    void shouldReturnBloodTestsOrderedByTimestampDesc() {
        UUID userId = UUID.randomUUID();
        String token = TestTokenFactory.createUserToken(userId);
        Instant now = Instant.now();

        // Create the MIDDLE test (Timestamp: now - 1 hour)
        factory.createBloodTest(
                userId,
                now.minus(1, ChronoUnit.HOURS),
                Map.of(
                        "GLU", new BigDecimal("88"),
                        "HGB", new BigDecimal("14.0")
                )
        );
        // Create the OLDEST test (Timestamp: now - 2 hours)
        factory.createBloodTest(
                userId,
                now.minus(2, ChronoUnit.HOURS),
                Map.of(
                        "CREA", new BigDecimal("1.0"),
                        "K", new BigDecimal("4.9"),
                        "CA", new BigDecimal("9.1")
                )
        );
        // Create the NEWEST test (Timestamp: now)
        factory.createBloodTest(
                userId,
                now,
                Map.of(
                        "GLU", new BigDecimal("91"),
                        "CREA", new BigDecimal("0.88")
                )
        );

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/blood-tests/mine")
                .then()
                .statusCode(200)
                .body("$", hasSize(3)) // Verify 3 blood tests are returned
                // ASSERT ORDERING (Most Recent First)
                // Index [0]: NEWEST TEST (GLU 91, CREA 0.88)
                .body("[0].results", hasSize(2))
                .body("[0].results.analyte", containsInAnyOrder("GLU", "CREA"))
                .body("[0].results.value", containsInAnyOrder(91, 0.88f))

                // Index [1]: MIDDLE TEST (GLU 88, HGB 14.0)
                .body("[1].results", hasSize(2))
                .body("[1].results.analyte", containsInAnyOrder("GLU", "HGB"))
                .body("[1].results.value", containsInAnyOrder(88, 14.0f))

                // Index [2]: OLDEST TEST (GLU 100)
                .body("[2].results", hasSize(3))
                .body("[2].results.analyte", containsInAnyOrder("CREA", "K", "CA"))
                .body("[2].results.value", containsInAnyOrder(1.0f, 4.9f, 9.1f));
    }

    @Test
    void shouldRejectWithoutToken() {
        given()
                .when()
                .get("/api/v1/blood-tests/mine")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldNotShowOthersTests() {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();

        String token = TestTokenFactory.createUserToken(user2);

        factory.createBloodTest(
                user1,
                Instant.now(),
                Map.of("GLUCOSE", new BigDecimal("90"))
        );

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/v1/blood-tests/mine")
                .then()
                .statusCode(404)
                .body("error", equalTo("NotFoundException"))
                .body("message", equalTo("Blood tests not found"))
                .body("timestamp", notNullValue())
                .body("traceId", notNullValue());
    }
}
