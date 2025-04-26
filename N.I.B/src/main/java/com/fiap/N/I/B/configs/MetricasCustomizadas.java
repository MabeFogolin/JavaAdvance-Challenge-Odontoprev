package com.fiap.N.I.B.configs;

import com.fiap.N.I.B.Repositories.UsuarioRepository;
import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricasCustomizadas {

    @Bean
    public MeterBinder quantidadeUsuarios(UsuarioRepository usuarioRepository) {
        return (meterRegistry) -> {
            Gauge.builder("user.count", usuarioRepository::count)
                    .register(meterRegistry);
        };
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
