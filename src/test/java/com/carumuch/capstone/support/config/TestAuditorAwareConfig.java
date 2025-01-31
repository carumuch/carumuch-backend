package com.carumuch.capstone.support.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.Optional;

@TestConfiguration
public class TestAuditorAwareConfig {
    @Bean
    public AuditorAware<String> userAuditorAware() {
        return () -> Optional.of("testLoginId");
    }

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
}
