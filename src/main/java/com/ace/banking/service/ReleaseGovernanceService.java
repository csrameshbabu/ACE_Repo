package com.ace.banking.service;

import com.ace.banking.model.PaymentRiskEntry;

import java.util.List;

public class ReleaseGovernanceService {
    public List<PaymentRiskEntry> getPaymentRiskMatrix() {
        return List.of(
                new PaymentRiskEntry(
                        "Beneficiary management",
                        "High impact: funds are sent to an unapproved beneficiary or fraudulent recipient",
                        "Medium",
                        "Eligibility checks, OTP verification, duplicate detection, review for unusual patterns, and audit logging",
                        "Policy evidence, test trace, and approval evidence attached to each release",
                        "Low after controls",
                        "Payments domain lead"
                ),
                new PaymentRiskEntry(
                        "Funds transfer",
                        "Critical: value loss, duplicate execution, or unauthorized payment transfer",
                        "High",
                        "Balance validation, idempotency keys, replay protection, reversal capability, concurrency checks, and dual approval for large value limits",
                        "Deterministic transaction tests, contract tests, and production-safe synthetic scenarios",
                        "Medium after controls",
                        "Digital banking product owner"
                ),
                new PaymentRiskEntry(
                        "AI policy assistant",
                        "Medium: incorrect advice or unsafe tool selection in a policy or account-management workflow",
                        "Medium",
                        "Grounded answer checks, citations, abstention, tool allow-list, and human approval for high-risk actions",
                        "Prompt regression suite, red-team tests, and judge-rubric scoring",
                        "Low after controls",
                        "Responsible AI reviewer"
                )
        );
    }
}
