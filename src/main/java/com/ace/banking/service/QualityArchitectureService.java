package com.ace.banking.service;

import com.ace.banking.model.BusinessScenario;
import com.ace.banking.model.QualityLayer;
import com.ace.banking.model.ReleaseGate;

import java.util.List;

public class QualityArchitectureService {
    public List<QualityLayer> getQualityLayers() {
        return List.of(
                QualityLayer.UNIT,
                QualityLayer.COMPONENT,
                QualityLayer.CONTRACT,
                QualityLayer.API,
                QualityLayer.INTEGRATION,
                QualityLayer.UI,
                QualityLayer.MOBILE,
                QualityLayer.PERFORMANCE,
                QualityLayer.SECURITY,
                QualityLayer.RESILIENCE,
                QualityLayer.ACCESSIBILITY
        );
    }

    public List<BusinessScenario> getBusinessScenarios() {
        return List.of(
                new BusinessScenario("Add beneficiary", "Eligibility, OTP, duplicate checks, limit enforcement, and delayed activation controls"),
                new BusinessScenario("Funds transfer", "Balance validation, fee checks, idempotency, reversal and concurrency controls, and completed payment audit trail"),
                new BusinessScenario("Transaction history", "Completeness, ordering, filtering, and privacy-safe retrieval"),
                new BusinessScenario("AI policy Q&A", "Grounded answers with explicit citations and abstention when evidence is insufficient"),
                new BusinessScenario("Agentic service request", "Authenticated tool use, approval for payment-changing actions, and auditable evidence"),
                new BusinessScenario("Mobile journey", "Cross-device, network-degradation, and accessibility assurance")
        );
    }

    public List<ReleaseGate> getMandatoryReleaseGates() {
        return List.of(
                new ReleaseGate("Deterministic golden-case validation", true, "Critical payment and policy scenarios must pass before release"),
                new ReleaseGate("Synthetic data privacy review", true, "No personal or production data is allowed in test suites"),
                new ReleaseGate("Traceable test-to-service evidence", true, "Service traces and release evidence must connect to the business flow"),
                new ReleaseGate("Human approval for payment-changing agent actions", true, "AI tools cannot submit a funds transfer without explicit authorization"),
                new ReleaseGate("Residual-risk sign-off", true, "Named owners must approve remaining risk and exceptions")
        );
    }

    public String buildExecutiveSummary() {
        return "The target-state architecture combines conventional QE, cloud-native assurance, and governed agentic AI " +
                "to protect funds transfer flows while improving release speed, observability, and accountability.";
    }
}
