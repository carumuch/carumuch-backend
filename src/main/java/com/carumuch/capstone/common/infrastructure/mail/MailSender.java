package com.carumuch.capstone.common.infrastructure.mail;

public interface MailSender {
	String MAIL_SUBJECT_PREFIX = "[카우머치]";
	String DOMAIN_NAME = "카우머치";
	String MAIL_CHARSET = "utf-8";
	String MAIL_SUBTYPE_HTML = "html";

	void send(String recipientAddress, String mailSubject, String mailContent);
}
