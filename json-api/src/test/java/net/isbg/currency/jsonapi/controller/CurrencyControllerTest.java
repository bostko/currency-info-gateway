package net.isbg.currency.jsonapi.controller;

import net.isbg.currency.jsonapi.dto.CurrencyRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import net.isbg.currency.jsonapi.exception.DuplicateRequestException;
import net.isbg.currency.jsonapi.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    @Mock CurrencyService currencyService;
    @InjectMocks CurrencyController controller;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    private static final String REQUEST_ID = "b89577fe-8c37-4962-8af3-7cb89a245160";
    private static final long TIMESTAMP = 1586335186721L;
    private static final String CLIENT = "1234";
    private static final String CURRENCY = "EUR";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    // POST /json_api/current — happy path

    @Test
    void current_validRequest_returnsLatestRate() throws Exception {
        var request = new CurrentRequest(REQUEST_ID, TIMESTAMP, CLIENT, CURRENCY);
        var rate = new CurrencyRateResponse(CURRENCY, new BigDecimal("1.0882"), TIMESTAMP);
        when(currencyService.getCurrent(any())).thenReturn(rate);

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value(CURRENCY))
                .andExpect(jsonPath("$.timestamp").value(TIMESTAMP));
    }

    // POST /json_api/current — duplicate requestId must return 409 Conflict

    @Test
    void current_duplicateRequestId_returnsConflict() throws Exception {
        var request = new CurrentRequest(REQUEST_ID, TIMESTAMP, CLIENT, CURRENCY);
        when(currencyService.getCurrent(any()))
                .thenThrow(new DuplicateRequestException(REQUEST_ID));

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }

    // POST /json_api/history — happy path; period is interpreted as hours

    @Test
    void history_validRequest_returnsRatesForPeriod() throws Exception {
        var request = new HistoryRequest(REQUEST_ID, TIMESTAMP, CLIENT, CURRENCY, 24);
        var rates = List.of(
                new CurrencyRateResponse(CURRENCY, new BigDecimal("1.0882"), TIMESTAMP - 3_600_000),
                new CurrencyRateResponse(CURRENCY, new BigDecimal("1.0901"), TIMESTAMP)
        );
        when(currencyService.getHistory(any())).thenReturn(rates);

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currency").value(CURRENCY))
                .andExpect(jsonPath("$[1].currency").value(CURRENCY));
    }

    // POST /json_api/history — duplicate requestId must return 409 Conflict

    @Test
    void history_duplicateRequestId_returnsConflict() throws Exception {
        var request = new HistoryRequest(REQUEST_ID, TIMESTAMP, CLIENT, CURRENCY, 24);
        when(currencyService.getHistory(any()))
                .thenThrow(new DuplicateRequestException(REQUEST_ID));

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }
}