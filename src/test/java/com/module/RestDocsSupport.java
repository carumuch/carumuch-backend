package com.module;

import com.carumuch.capstone.domain.auth.controller.AuthController;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.domain.auth.service.AuthService;
import com.carumuch.capstone.domain.auth.service.CustomOAuth2UserService;
import com.carumuch.capstone.global.config.SecurityConfig;
import com.carumuch.capstone.support.config.RestDocsConfig;
import com.carumuch.capstone.support.config.TestAuditorAwareConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        AuthController.class
//        UserController.class,
//        ImageController.class,
//        BoardController.class,
//        VehicleController.class,
//        EstimateController.class,
//        BidController.class,
//        BodyShopController.class
})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import({SecurityConfig.class,TestAuditorAwareConfig.class, RestDocsConfig.class})
public abstract class RestDocsSupport {

    @Autowired
    protected RestDocumentationResultHandler restDocsHandler;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    protected BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    protected AuthService authService;
}
