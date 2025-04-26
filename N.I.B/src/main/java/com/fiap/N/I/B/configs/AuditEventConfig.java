package com.fiap.N.I.B.configs;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditEventConfig {

    @Bean(name = "auditEventLog")
    public AuditEventRepository  auditEventConfig() {
        return new InMemoryAuditEventRepository();
    }
}
