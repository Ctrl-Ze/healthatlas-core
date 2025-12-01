package com.healthatlas.core.bloodtests.service;


import com.healthatlas.core.bloodtests.adapters.inbound.rest.dto.request.BloodTestDto;
import com.healthatlas.core.bloodtests.adapters.outbound.db.AnalyteRepositoryAdapter;
import com.healthatlas.core.bloodtests.domain.model.Analyte;
import com.healthatlas.core.bloodtests.domain.model.BloodTest;
import com.healthatlas.core.bloodtests.domain.model.BloodTestResult;
import com.healthatlas.core.bloodtests.ports.inbound.BloodTestUseCase;
import com.healthatlas.core.bloodtests.ports.outbound.AnalyteLookupPort;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestRepositoryPort;
import com.healthatlas.core.bloodtests.ports.outbound.BloodTestResultRepositoryPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class BloodTestServiceTest {

    @InjectMock
    AnalyteRepositoryAdapter analyteRepository;

    @InjectMock
    BloodTestRepositoryPort bloodTestRepository;

    @InjectMock
    BloodTestResultRepositoryPort bloodTestResultRepository;

    @InjectMock
    AnalyteLookupPort analyteCacheAdapter;

    @Inject
    BloodTestUseCase bloodTestService;

    @Test
    void shouldCreateBloodTestSuccessfully() {
        //given
        UUID userId = UUID.randomUUID();

        var resultHgb = new BloodTestDto.ResultDto();
        resultHgb.analyte = "HGB";
        resultHgb.value = new BigDecimal("13.5");

        var resultRbc = new BloodTestDto.ResultDto();
        resultRbc.analyte = "RBC";
        resultRbc.value = new BigDecimal("4.8");

        var dto = new BloodTestDto();
        dto.timestamp = Instant.now();
        dto.confirmed = false;
        dto.results = List.of(resultHgb, resultRbc);

        //TODO create complete analytes
        Analyte hgb = new Analyte();
        hgb.id = UUID.randomUUID();
        hgb.name = "HGB";

        Analyte rbc = new Analyte();
        rbc.id = UUID.randomUUID();
        rbc.name = "RBC";


        //when
        Mockito.when(analyteCacheAdapter.getByName("HGB")).thenReturn(hgb);
        Mockito.when(analyteCacheAdapter.getByName("RBC")).thenReturn(rbc);

        // Capture arguments going into persist()
        ArgumentCaptor<BloodTest> bloodTestCaptor = ArgumentCaptor.forClass(BloodTest.class);
        ArgumentCaptor<List<BloodTestResult>> resultsCaptor = ArgumentCaptor.forClass(List.class);


        var response = bloodTestService.createBloodTest(userId, dto);

        //then
        assertNotNull(response);
        assertEquals(2, response.results().size());

        // Verify BloodTestResult persisted
        verify(bloodTestRepository, times(1)).save(bloodTestCaptor.capture());
        BloodTest persisted = bloodTestCaptor.getValue();

        assertEquals(dto.timestamp, persisted.timestamp);
        assertEquals(dto.confirmed, persisted.confirmed);
        assertEquals(userId, persisted.userRef);

        // Verify BloodTestResult persisted
        verify(bloodTestResultRepository, times(1)).save(resultsCaptor.capture());
        List<BloodTestResult> persistedResults = resultsCaptor.getValue();

        assertEquals(2, persistedResults.size());
        assertSame(persisted, persistedResults.get(0).bloodTest);
        assertSame(persisted, persistedResults.get(1).bloodTest);

        // Verify analytes were looked up
        verify(analyteCacheAdapter).getByName("HGB");
        verify(analyteCacheAdapter).getByName("RBC");

        // Validate DTO mapping
        assertEquals("HGB", response.results().get(0).analyte());
        assertEquals("RBC", response.results().get(1).analyte());

        verifyNoMoreInteractions(bloodTestRepository, bloodTestResultRepository);
    }
}