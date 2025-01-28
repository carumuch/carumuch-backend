package com.module;

import com.carumuch.capstone.domain.auth.controller.AuthController;
import com.carumuch.capstone.domain.auth.jwt.TokenProvider;
import com.carumuch.capstone.domain.bid.controller.BidController;
import com.carumuch.capstone.domain.board.controller.BoardController;
import com.carumuch.capstone.domain.bodyshop.controller.BodyShopController;
import com.carumuch.capstone.domain.estimate.controller.EstimateController;
import com.carumuch.capstone.domain.image.controller.ImageController;
import com.carumuch.capstone.domain.user.controller.UserController;
import com.carumuch.capstone.domain.vehicle.controller.VehicleController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = {
        AuthController.class,
        UserController.class,
        ImageController.class,
        BoardController.class,
        VehicleController.class,
        EstimateController.class,
        BidController.class,
        BodyShopController.class
})
@AutoConfigureMockMvc
public abstract class RestDocsSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected TokenProvider tokenProvider;

    @BeforeEach
    void setUpMockMvcForRestDocs(WebApplicationContext webApplicationContext,
                                 RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
                .build();
    }
}
