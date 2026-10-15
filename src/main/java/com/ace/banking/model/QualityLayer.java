package com.ace.banking.model;

public enum QualityLayer {
    UNIT("Unit assurance", "Smallest verification of logic and business rules"),
    COMPONENT("Component assurance", "Service and module behavior in isolation"),
    CONTRACT("Contract assurance", "Consumer-provider API and event compatibility"),
    API("API assurance", "Endpoint validation, schema, and authorization checks"),
    INTEGRATION("Integration assurance", "Cross-service flow and data consistency"),
    UI("UI assurance", "Workflow validation across web screens and journeys"),
    MOBILE("Mobile assurance", "Device, OS, and accessibility coverage"),
    PERFORMANCE("Performance assurance", "Capacity, throughput, and resilience under load"),
    SECURITY("Security assurance", "Identity, authorization, and payment protection"),
    RESILIENCE("Resilience assurance", "Retry, circuit breaking, and disaster recovery"),
    ACCESSIBILITY("Accessibility assurance", "Inclusive experience for all customers");

    private final String name;
    private final String purpose;

    QualityLayer(String name, String purpose) {
        this.name = name;
        this.purpose = purpose;
    }

    public String getName() {
        return name;
    }

    public String getPurpose() {
        return purpose;
    }
}
