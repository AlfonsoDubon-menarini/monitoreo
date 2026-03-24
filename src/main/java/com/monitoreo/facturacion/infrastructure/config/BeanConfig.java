package com.monitoreo.facturacion.infrastructure.config;

import com.monitoreo.facturacion.domain.service.EvaluadorSalud;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public EvaluadorSalud evaluadorSalud() {
        return new EvaluadorSalud();
    }
}
