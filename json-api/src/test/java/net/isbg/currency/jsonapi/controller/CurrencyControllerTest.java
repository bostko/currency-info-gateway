package net.isbg.currency.jsonapi.controller;

import net.isbg.currency.domain.entity.Rates;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import net.isbg.currency.jsonapi.exception.DuplicateRequestException;
import net.isbg.currency.jsonapi.service.AuditService;
import net.isbg.currency.jsonapi.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CurrencyService currencyService;

    @MockitoBean
    AuditService auditService;

    // epoch-seconds stored in Rates; controller multiplies by 1000 for the response timestamp
    private static final long RATE_TIMESTAMP_SECS = 1776999887L;
    private static final LocalDate RATE_DATE = LocalDate.of(2026, 4, 21);

    private static Rates gbpRates(double gbpRate) {
        return new Rates(RATE_TIMESTAMP_SECS, "EUR", RATE_DATE, Map.of("GBP", gbpRate, "USD", 1.08));
    }

    // --- POST /json_api/current ---

    @Test
    void current_validRequest_returnsRateForCurrency() throws Exception {
        String requestId = UUID.randomUUID().toString();
        when(currencyService.getCurrent(any())).thenReturn(gbpRates(0.856));

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"requestId":"%s","timestamp":1776999887861,"client":"1234","currency":"GBP"}
                                """.formatted(requestId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("GBP"))
                .andExpect(jsonPath("$.date").value(RATE_DATE.toString()))
                .andExpect(jsonPath("$.timestamp").value(RATE_TIMESTAMP_SECS * 1000))
                .andExpect(jsonPath("$.rates.GBP").value(1.0))
                .andExpect(jsonPath("$.rates.USD").exists());
    }

    @Test
    void current_duplicateRequestId_returnsConflict() throws Exception {
        String requestId = UUID.randomUUID().toString();
        doThrow(new DuplicateRequestException(requestId))
                .when(auditService).audit(any(CurrentRequest.class));

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"requestId":"%s","timestamp":1776999887861,"client":"1234","currency":"GBP"}
                                """.formatted(requestId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    // --- POST /json_api/history ---

    @Test
    void history_validRequest_returnsRatesListForPeriod() throws Exception {
        String requestId = UUID.randomUUID().toString();
        when(currencyService.getHistory(any(), any(Pageable.class)))
                .thenReturn(List.of(gbpRates(0.855), gbpRates(0.857)));

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"requestId":"%s","timestamp":1777000097459,"client":"1234","currency":"GBP","period":24}
                                """.formatted(requestId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currency").value("GBP"))
                .andExpect(jsonPath("$[0].rates.GBP").exists())
                .andExpect(jsonPath("$[1].currency").value("GBP"));
    }

    @Test
    void history_defaultPageable_usesPageZeroAndDefaultSize() throws Exception {
        when(currencyService.getHistory(any(), any(Pageable.class))).thenReturn(List.of());

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"requestId":"%s","timestamp":1777000097459,"client":"1234","currency":"GBP","period":24}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(currencyService).getHistory(any(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isZero();
        assertThat(pageable.getPageSize()).isPositive();
    }

    @Test
    void history_customPageAndSize_passedToService() throws Exception {
        when(currencyService.getHistory(any(), any(Pageable.class))).thenReturn(List.of());

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "2")
                        .param("size", "5")
                        .content("""
                                {"requestId":"%s","timestamp":1777000097459,"client":"1234","currency":"GBP","period":24}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(currencyService).getHistory(any(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(5);
    }

    @Test
    void history_pageOneWithSize3_returnsCorrectSlice() throws Exception {
        List<Rates> page1Results = List.of(gbpRates(0.860), gbpRates(0.861), gbpRates(0.862));
        when(currencyService.getHistory(any(), any(Pageable.class))).thenReturn(page1Results);

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "3")
                        .content("""
                                {"requestId":"%s","timestamp":1777000097459,"client":"1234","currency":"GBP","period":24}
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].currency").value("GBP"));
    }

    @Test
    void history_duplicateRequestId_returnsConflict() throws Exception {
        String requestId = UUID.randomUUID().toString();
        doThrow(new DuplicateRequestException(requestId))
                .when(auditService).audit(any(HistoryRequest.class));

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"requestId":"%s","timestamp":1777000097459,"client":"1234","currency":"GBP","period":24}
                                """.formatted(requestId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }
}