package com.carumuch.capstone.support;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.carumuch.capstone.support.config.AsyncTestConfig;

@SpringBootTest
@Transactional
@Import(AsyncTestConfig.class)
@ActiveProfiles("test")
@Tag("integration")
public abstract class IntegrationSupportTest {

}
