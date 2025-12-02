package com.carumuch.capstone.identity.infrastructure.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.carumuch.capstone.identity.application.RefreshTokenStore;
import com.carumuch.capstone.identity.domain.user.UserWithdrawnEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserWithdrawnEventHandler {

	private final RefreshTokenStore refreshTokenStore;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onUserWithdrawn(UserWithdrawnEvent event) {
		refreshTokenStore.delete(event.loginId());
	}
}
