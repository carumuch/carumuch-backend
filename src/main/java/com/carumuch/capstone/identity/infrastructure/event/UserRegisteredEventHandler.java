package com.carumuch.capstone.identity.infrastructure.event;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.carumuch.capstone.common.infrastructure.mail.MailSender;
import com.carumuch.capstone.common.infrastructure.mail.TemplateRenderer;
import com.carumuch.capstone.identity.domain.user.UserRegisteredEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventHandler {
    private static final String WELCOME_MAIL_SUBJECT = "회원가입에 감사드립니다";
	private static final String WELCOME_MAIL = "mail/user/welcome";

	private final MailSender mailSender;
	private final TemplateRenderer templateRenderer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserRegistered(UserRegisteredEvent event) {
		String emailContent = templateRenderer
			.render(WELCOME_MAIL, Map.of("name", event.user().getName()));

		mailSender.send(event.user().getEmail(), WELCOME_MAIL_SUBJECT, emailContent);
	}
}
