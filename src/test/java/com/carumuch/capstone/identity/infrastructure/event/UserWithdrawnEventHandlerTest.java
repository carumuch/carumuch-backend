package com.carumuch.capstone.identity.infrastructure.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.carumuch.capstone.identity.application.RefreshTokenStore;
import com.carumuch.capstone.identity.domain.user.UserWithdrawnEvent;
import com.carumuch.capstone.support.IntegrationSupportTest;

class UserWithdrawnEventHandlerTest extends IntegrationSupportTest {

	@Autowired
	UserWithdrawnEventHandler userWithdrawnEventHandler;

	@MockitoBean
	RefreshTokenStore redisTokenService;

	@Nested
	@DisplayName("회원 탈퇴 이벤트 발생")
	class onUserWithdrawn {

		@Test
		 void 회원_토큰을_삭제한다() {
		    //given
		    String loginId = "testLoginId";
			UserWithdrawnEvent event = new UserWithdrawnEvent(loginId);

		    //when
		    userWithdrawnEventHandler.onUserWithdrawn(event);

		    //then
			Mockito.verify(redisTokenService).delete(loginId);
		}
	}
}