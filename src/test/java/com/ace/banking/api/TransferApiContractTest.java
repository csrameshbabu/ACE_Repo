package com.ace.banking.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.ace.banking.BankingApiApplication.class)
@AutoConfigureMockMvc
class TransferApiContractTest {
    private static final Pattern TRACEPARENT =
            Pattern.compile("^00-([0-9a-f]{32})-[0-9a-f]{16}-[0-9a-f]{2}$");
    private static final String REQUEST_TRACEPARENT =
            "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void providerHonorsConsumerContractAndReturnsTraceCorrelation() throws Exception {
        JsonNode contract = objectMapper.readTree(
                new ClassPathResource("contracts/transfers-v1-consumer.json").getInputStream()
        );
        JsonNode request = contract.path("request");
        JsonNode expected = contract.path("response");
        String key = request.path("body").path("idempotencyKey").asText();

        MvcResult result = mockMvc.perform(post(request.path("path").asText())
                        .contentType(request.path("contentType").asText())
                        .header("Idempotency-Key", key)
                        .header("traceparent", REQUEST_TRACEPARENT)
                        .content(objectMapper.writeValueAsBytes(request.path("body"))))
                .andExpect(status().isAccepted())
                .andExpect(content().contentTypeCompatibleWith(expected.path("contentType").asText()))
                .andExpect(jsonPath("$.status").value(expected.path("statusValue").asText()))
                .andReturn();

        JsonNode actual = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        for (JsonNode field : expected.path("requiredFields")) {
            assertTrue(actual.has(field.asText()), "Contract response is missing " + field.asText());
        }
        var traceparent = TRACEPARENT.matcher(result.getResponse().getHeader("traceparent"));
        assertTrue(traceparent.matches(), "Response must propagate a W3C traceparent");
        assertEquals(REQUEST_TRACEPARENT.substring(3, 35), traceparent.group(1),
                "Provider must continue the incoming trace ID");
    }

    @Test
    void providerRejectsInvalidTransferAndConflictingIdempotencyReuse() throws Exception {
        mockMvc.perform(post("/api/v1/transfers")
                        .contentType("application/json")
                        .header("Idempotency-Key", "invalid-contract")
                        .content("""
                                {"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001",
                                 "amount":0,"currency":"USD","idempotencyKey":"invalid-contract"}
                                """))
                .andExpect(status().isBadRequest());

        MvcResult first = mockMvc.perform(post("/api/v1/transfers")
                        .contentType("application/json")
                        .header("Idempotency-Key", "idempotency-contract")
                        .content("""
                                {"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001",
                                 "amount":5,"currency":"USD","idempotencyKey":"idempotency-contract"}
                                """))
                .andExpect(status().isAccepted())
                .andReturn();

        MvcResult replay = mockMvc.perform(post("/api/v1/transfers")
                        .contentType("application/json")
                        .header("Idempotency-Key", "idempotency-contract")
                        .content("""
                                {"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001",
                                 "amount":5,"currency":"USD","idempotencyKey":"idempotency-contract"}
                                """))
                .andExpect(status().isAccepted())
                .andReturn();
        assertEquals(objectMapper.readTree(first.getResponse().getContentAsByteArray()).path("transferId"),
                objectMapper.readTree(replay.getResponse().getContentAsByteArray()).path("transferId"));

        mockMvc.perform(post("/api/v1/transfers")
                        .contentType("application/json")
                        .header("Idempotency-Key", "idempotency-contract")
                        .content("""
                                {"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001",
                                 "amount":6,"currency":"USD","idempotencyKey":"idempotency-contract"}
                                """))
                .andExpect(status().isConflict());
    }
}
