package com.ace.banking.agent;

import java.util.Set;

public record HumanApproval(
        String approverSubject,
        String approvalReference,
        boolean approved,
        Set<String> authorities
) {
    public HumanApproval {
        authorities = Set.copyOf(authorities);
    }
}
