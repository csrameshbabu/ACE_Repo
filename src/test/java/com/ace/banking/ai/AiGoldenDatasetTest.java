package com.ace.banking.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AiGoldenDatasetTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void goldenSetHasCriticalGroundingAbstentionAndToolSafetyCases() throws Exception {
        List<JsonNode> cases = readCases();

        assertTrue(cases.size() >= 6);
        assertTrue(cases.stream().anyMatch(testCase ->
                "ABSTAIN".equals(testCase.path("expectedBehavior").asText())));
        assertTrue(cases.stream().anyMatch(testCase ->
                testCase.path("expectedBehavior").asText().contains("TOOL_CALL")));
        assertTrue(cases.stream().anyMatch(testCase ->
                "critical".equals(testCase.path("risk").asText())));
        assertTrue(cases.stream().allMatch(testCase ->
                testCase.hasNonNull("id") && testCase.hasNonNull("prompt") && testCase.hasNonNull("retrievedDocuments")));
    }

    @Test
    void judgeRubricKeepsDeterministicAndHumanGates() throws Exception {
        JsonNode rubric = objectMapper.readTree(
                new ClassPathResource("ai/judge-rubric.json").getInputStream()
        );

        assertEquals(5, rubric.path("dimensions").size());
        assertTrue(rubric.path("releaseGates").path("minimumWeightedScore").asDouble() >= 3.0);
        assertTrue(rubric.path("releaseGates").path("humanAdjudicationRequired").size() > 0);
        assertTrue(rubric.path("releaseGates").path("zeroTolerance").size() > 0);
    }

    private List<JsonNode> readCases() throws Exception {
        List<JsonNode> cases = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("ai/prompt-rag-golden-set.jsonl").getInputStream(),
                StandardCharsets.UTF_8
        ))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    cases.add(objectMapper.readTree(line));
                }
            }
        }
        return cases;
    }
}
