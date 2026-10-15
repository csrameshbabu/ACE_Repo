package com.ace.banking;

import com.ace.banking.model.ReleaseGate;
import com.ace.banking.service.QualityArchitectureService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QualityArchitectureServiceTest {
    @Test
    void qualityLayersContainMandatoryAssuranceStack() {
        QualityArchitectureService service = new QualityArchitectureService();

        assertEquals(11, service.getQualityLayers().size());
        assertTrue(service.getQualityLayers().stream().anyMatch(layer -> layer.name().equals("SECURITY")));
        assertTrue(service.getQualityLayers().stream().anyMatch(layer -> layer.name().equals("ACCESSIBILITY")));
    }

    @Test
    void releaseGatesIncludeCriticalControls() {
        QualityArchitectureService service = new QualityArchitectureService();

        List<ReleaseGate> gates = service.getMandatoryReleaseGates();
        assertTrue(gates.stream().anyMatch(gate -> gate.name().contains("golden-case")));
        assertTrue(gates.stream().anyMatch(gate -> gate.name().contains("Human approval")));
        assertTrue(gates.stream().allMatch(ReleaseGate::mandatory));
    }
}
