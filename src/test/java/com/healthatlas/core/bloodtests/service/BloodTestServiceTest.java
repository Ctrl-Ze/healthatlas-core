package com.healthatlas.core.bloodtests.service;


import com.healthatlas.core.bloodtests.dto.BloodTestFlatRowDto;
import com.healthatlas.core.bloodtests.dto.response.BloodTestResponseDto;
import com.healthatlas.core.bloodtests.dto.response.BloodTestResultDto;
import com.healthatlas.core.bloodtests.repository.AnalyteRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestRepository;
import com.healthatlas.core.bloodtests.repository.BloodTestResultRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@QuarkusTest
class BloodTestServiceTest {

    @InjectMock
    AnalyteRepository analyteRepository;

    @InjectMock
    BloodTestRepository bloodTestRepository;

    @InjectMock
    BloodTestResultRepository bloodTestResultRepository;

    @Inject
    BloodTestService bloodTestService;

    @Test
    void shouldReturnBloodTestsForUser() {
        // given
        UUID userId = UUID.randomUUID();
        UUID bloodTestId = UUID.randomUUID();
        UUID bloodTestId2 = UUID.randomUUID();

        SecurityContext ctx = Mockito.mock(SecurityContext.class);
        JsonWebToken jwt = Mockito.mock(JsonWebToken.class);

        var now = Instant.now();
        var tomorrow = now.plus(1, ChronoUnit.DAYS);

        var rows = List.of(
                new BloodTestFlatRowDto(bloodTestId, false, tomorrow, now, now, "HGB",
                        new BigDecimal("13.5"), "g/dL", new BigDecimal("12.0"), new BigDecimal("17.5")),
                new BloodTestFlatRowDto(bloodTestId, false, tomorrow, now, now, "RBC",
                        new BigDecimal("4.7"), "10^6/uL", new BigDecimal("4.2"), new BigDecimal("5.9")),
                new BloodTestFlatRowDto(bloodTestId2, false, now, now, now, "HGB",
                        new BigDecimal("16"), "g/dL", new BigDecimal("12.0"), new BigDecimal("17.5")),
                new BloodTestFlatRowDto(bloodTestId2, false, now, now, now, "RBC",
                        new BigDecimal("5"), "10^6/uL", new BigDecimal("4.2"), new BigDecimal("5.9"))
        );

        // when
        Mockito.when(ctx.getUserPrincipal()).thenReturn(jwt);
        Mockito.when(jwt.getClaim("user_id")).thenReturn(userId.toString());

        Mockito.when(bloodTestRepository.findDetailedBloodTestsByUserId(userId)).thenReturn(rows);

        var response = bloodTestService.getBloodTestsPerUser(ctx);

        //then
        assertNotNull(response);
        verify(bloodTestRepository, times(1)).findDetailedBloodTestsByUserId((userId));
        assertEquals(2, response.size());
        assertEquals(2, response.get(0).results().size());
        assertEquals(2, response.get(1).results().size());

        //Validate First test
        assertResponseMatches(rows.get(0), response.get(0));
        assertResultMatches(rows.get(0), response.get(0).results().get(0));
        assertResponseMatches(rows.get(1), response.get(0));
        assertResultMatches(rows.get(1), response.get(0).results().get(1));

        //Validate Second test
        assertResponseMatches(rows.get(2), response.get(1));
        assertResultMatches(rows.get(2), response.get(1).results().get(0));
        assertResponseMatches(rows.get(3), response.get(1));
        assertResultMatches(rows.get(3), response.get(1).results().get(1));
    }

    private void assertResponseMatches(BloodTestFlatRowDto source, BloodTestResponseDto response) {
        assertEquals(source.id(), response.id());
        assertEquals(source.confirmed(), response.confirmed());
        assertEquals(source.timestamp(), response.timestamp());
        assertEquals(source.updatedAt(), response.updatedAt());
        assertEquals(source.createdAt(), response.createdAt());
    }

    private void assertResultMatches(BloodTestFlatRowDto source, BloodTestResultDto result) {
        assertEquals(source.analyteName(), result.analyte());
        assertEquals(source.value(), result.value());
        assertEquals(source.unit(), result.unit());
        assertEquals(source.normalLow(), result.normalLow());
        assertEquals(source.normalHigh(), result.normalHigh());
    }
}