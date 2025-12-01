package com.carumuch.capstone.common.presentation;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.support.RestDocsSupport;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthCheckControllerTest extends RestDocsSupport {
	private static final String BASE_SUCCESS_MESSAGE = "OK";
	private static final String TEAM_MD_PATH = "docs/team-api-reference.md" ;

    @Test
    void 서버_상태_체크_2XX() throws Exception {
        //given & when
        ResultActions actions = mockMvc.perform(get("/health"));

        //then
        actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value(BASE_SUCCESS_MESSAGE))
			.andExpect(jsonPath("$.data").isEmpty())
			.andDo(restDocsHandler.document(ResourceDocumentation.resource(ResourceSnippetParameters.builder()
					.tag("🚗 [카우머치 TEAM Workspace]")
					.summary("카우머치 개발자를 위한 공용 정보")
					.description(readMarkdown(TEAM_MD_PATH))
					.responseSchema(Schema.schema(ApiResponse.class.getSimpleName()))
					.build())
				)
			);
    }
}