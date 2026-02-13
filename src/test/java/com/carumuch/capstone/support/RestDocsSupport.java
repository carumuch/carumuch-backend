package com.carumuch.capstone.support;

import static org.junit.jupiter.api.Assertions.*;

import com.carumuch.capstone.damage.application.DamageReportService;
import com.carumuch.capstone.damage.application.VehicleService;
import com.carumuch.capstone.damage.presentation.DamageReportController;
import com.carumuch.capstone.damage.presentation.VehicleController;
import com.carumuch.capstone.estimate.application.EstimateService;
import com.carumuch.capstone.estimate.presentation.EstimateController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.carumuch.capstone.identity.application.AccountRecoveryService;
import com.carumuch.capstone.common.infrastructure.config.SecurityConfig;
import com.carumuch.capstone.identity.presentation.AuthController;
import com.carumuch.capstone.identity.infrastructure.security.AccessDeniedHandlerImpl;
import com.carumuch.capstone.identity.infrastructure.security.AuthenticationEntryPointImpl;
import com.carumuch.capstone.identity.presentation.resolver.AuthUserResolver;
import com.carumuch.capstone.identity.application.AuthService;
import com.carumuch.capstone.identity.infrastructure.jwt.JwtTokenProvider;
import com.carumuch.capstone.common.infrastructure.logging.ExecutionTimeLogger;
import com.carumuch.capstone.identity.presentation.UserController;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;
import com.carumuch.capstone.identity.application.UserService;
import com.carumuch.capstone.common.presentation.HealthCheckController;
import com.carumuch.capstone.support.config.RestDocsConfig;
import com.carumuch.capstone.support.fixture.UserFixture;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = {
	HealthCheckController.class,
	UserController.class,
	AuthController.class,
	VehicleController.class,
	DamageReportController.class,
	EstimateController.class
})
@Import({
	SecurityConfig.class,
	ExecutionTimeLogger.class,
	RestDocsConfig.class
})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public abstract class RestDocsSupport {

	protected static final String BASE_SUCCESS_MESSAGE = "OK";
	protected static final String BASE_FIELD_ERROR_MESSAGE = "의 필드 값 유효하지 않습니다.";

    @Value("${cookie.name}")
    protected String REFRESH_TOKEN_COOKIE_NAME;

    @Autowired
    protected RestDocumentationResultHandler restDocsHandler;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    protected AuthenticationEntryPointImpl authenticationEntryPoint;

    @MockitoBean
    protected AccessDeniedHandlerImpl accessDeniedHandler;

    @MockitoBean
    protected AuthUserResolver authUserResolver;

    @MockitoBean
    protected UserRepository userRepository;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected AuthService authService;

	@MockitoBean
	protected AccountRecoveryService accountRecoveryService;

	@MockitoBean
	protected VehicleService vehicleService;

	@MockitoBean
	protected DamageReportService damageReportService;

	@MockitoBean
	protected EstimateService estimateService;

    @BeforeEach
    void setUp() {
        User userFixture = UserFixture.USER_FIXTURE_1.create();
		ReflectionTestUtils.setField(userFixture, "id", 1L);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userFixture.getLoginId(), null, List.of())
        );

        Mockito.when(userRepository.findByLoginId(userFixture.getLoginId()))
                .thenReturn(Optional.of(userFixture));
        Mockito.when(authUserResolver.resolveArgument(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(userFixture);
    }

	protected String readMarkdown(String path) {
		ClassPathResource resource = new ClassPathResource(path);
		try {
			return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			fail("문서를 읽어들이는 도중 예외가 발생했습니다. : " + path);
			return null;
		}
	}
}
