package com.ace.banking.agent;

import java.util.Set;

public record AgentPrincipal(String subject, Set<String> authorities) {
    public AgentPrincipal {
        authorities = Set.copyOf(authorities);
    }
}
