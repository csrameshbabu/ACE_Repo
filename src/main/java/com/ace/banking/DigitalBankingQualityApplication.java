package com.ace.banking;

import com.ace.banking.model.BusinessScenario;
import com.ace.banking.model.QualityLayer;
import com.ace.banking.model.ReleaseGate;
import com.ace.banking.service.AiAssuranceService;
import com.ace.banking.service.QualityArchitectureService;
import com.ace.banking.service.ReleaseGovernanceService;

public class DigitalBankingQualityApplication {
    public static void main(String[] args) {
        QualityArchitectureService architectureService = new QualityArchitectureService();
        AiAssuranceService aiAssuranceService = new AiAssuranceService();
        ReleaseGovernanceService governanceService = new ReleaseGovernanceService();

        System.out.println("=== Digital Banking Quality Architecture ===");
        System.out.println(architectureService.buildExecutiveSummary());

        System.out.println("\nQuality layers:");
        for (QualityLayer layer : architectureService.getQualityLayers()) {
            System.out.println("- " + layer.getName() + ": " + layer.getPurpose());
        }

        System.out.println("\nCritical scenarios:");
        for (BusinessScenario scenario : architectureService.getBusinessScenarios()) {
            System.out.println("- " + scenario.scenario() + " -> " + scenario.requiredAssurance());
        }

        System.out.println("\nMandatory release gates:");
        for (ReleaseGate gate : architectureService.getMandatoryReleaseGates()) {
            System.out.println("- " + gate.name() + " (mandatory=" + gate.mandatory() + ")");
        }

        System.out.println("\nAI assurance checks:");
        for (String item : aiAssuranceService.getRequiredChecks()) {
            System.out.println("- " + item);
        }

        System.out.println("\nPayment risk summary:");
        governanceService.getPaymentRiskMatrix().forEach(entry ->
                System.out.println("- " + entry.flow() + " => residual risk " + entry.residualRisk() + ", owner: " + entry.owner())
        );
    }
}
