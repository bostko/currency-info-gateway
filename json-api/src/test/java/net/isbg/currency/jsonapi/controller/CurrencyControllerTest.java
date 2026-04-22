package net.isbg.currency.jsonapi.controller;

import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String REQUEST_ID = "b89577fe-8c37-4962-8af3-7cb89a245160";
    private static final long TIMESTAMP = 1586335186721L;
    private static final String CLIENT = "1234";
    private static final String CURRENCY = "EUR";

    // POST /json_api/current — happy path

    @Test
    void current_validRequest_returnsLatestRate() throws Exception {
        var request = new CurrentRequest(REQUEST_ID, Instant.now(), CLIENT, CURRENCY);

        mockMvc.perform(post("/json_api/current")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currency").value(CURRENCY))
                .andExpect(jsonPath("$.timestamp").value(greaterThan(0L)));
    }

    // POST /json_api/current — duplicate requestId must return 409 Conflict

    @Test
    void current_duplicateRequestId_returnsConflict() throws Exception {
        var request = new CurrentRequest(REQUEST_ID, Instant.now(), CLIENT, CURRENCY);

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
        var now = Instant.now();

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

        mockMvc.perform(post("/json_api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }
}