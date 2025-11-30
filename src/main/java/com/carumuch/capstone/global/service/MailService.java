package com.carumuch.capstone.global.service;

import com.carumuch.capstone.global.exception.ErrorCode;
import com.carumuch.capstone.global.exception.CustomException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@Service
public class MailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    private final String MAIL_ADDRESS;
    private final String HOST_ADDRESS;

    public MailService(JavaMailSender emailSender,
                       SpringTemplateEngine templateEngine,
                       @Value("${spring.mail.host}") String HOST_ADDRESS,
                       @Value("${spring.mail.username}") String MAIL_ADDRESS) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.HOST_ADDRESS = "@" + HOST_ADDRESS.replace("smtp.", "");
        this.MAIL_ADDRESS = MAIL_ADDRESS;
    }

    /**
     * 인증코드 생성 -> 0 부터 9 까지 수를 6자리 숫자로 조합
     */
    public String createCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int numbers = random.nextInt(10);
            code.append(numbers);
        }

        return code.toString();
    }

    /**
     * 비밀번호 찾기
     */
    public void sendVerificationCodeMail(String name, String email, String code){
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[카우머치] " + name + "님 인증 번호 안내드립니다."); // 이메일 제목
            message.setText(setVerificationCodeContext(code), "utf-8", "html"); // 내용 설정

            // 메일 이름 설정
             message.setFrom(new InternetAddress(MAIL_ADDRESS + HOST_ADDRESS, "카우머치"));

            emailSender.send(message); // 이메일 전송

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String setVerificationCodeContext(String code) { // 타임리프 설정
        Context context = new Context();
        context.setVariable("code", code); // Template 전달 데이터
        return templateEngine.process("verificationCodeMail", context); // verificationCodeMail.html
    }

    /**
     * 아이디 찾기
     */
    public void sendFindLoginIdMail(String name, String email, String loginId){
        try {
            MimeMessage message = emailSender.createMimeMessage();

            message.addRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
            message.setSubject("[카우머치] " + name + "님 아이디 찾기 안내드립니다."); // 이메일 제목
            message.setText(setFindLoginIdContext(loginId), "utf-8", "html"); // 내용 설정

            // 메일 이름 설정
            message.setFrom(new InternetAddress(MAIL_ADDRESS + HOST_ADDRESS, "카우머치"));

            emailSender.send(message); // 이메일 전송

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String setFindLoginIdContext(String loginId) { // 타임리프 설정
        Context context = new Context();
        context.setVariable("loginId", loginId); // Template 전달 데이터
        return templateEngine.process("findLoginIdMail", context); // findLoginIdMail.html
    }

}
