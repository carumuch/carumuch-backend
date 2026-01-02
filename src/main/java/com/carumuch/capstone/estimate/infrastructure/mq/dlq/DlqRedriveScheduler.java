package com.carumuch.capstone.estimate.infrastructure.mq.dlq;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.carumuch.capstone.estimate.infrastructure.mq.config.RabbitMqConfig;
import com.carumuch.capstone.estimate.infrastructure.mq.message.EstimateResultMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DlqRedriveScheduler {

	private static final int MAX_ATTEMPTS = 10;

	private final DlqMessageRepository dlqMessageRepository;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	@Scheduled(fixedDelay = 10_000)
	@Transactional
	public void redriveEstimateResultDlq() {
		LocalDateTime now = LocalDateTime.now();

		List<DlqMessage> dlqMessages = dlqMessageRepository.findRetryMessages(
			DlqStatus.PENDING,
			now,
			PageRequest.of(0, 50)
		);

		for (DlqMessage dlqMessage : dlqMessages) {
			if (dlqMessage.getAttemptCount() >= MAX_ATTEMPTS) {
				log.error("DLQ Give Up 발생: messageId={} originalQueue={} payload={}",
					dlqMessage.getId(),
					dlqMessage.getOriginalQueue(),
					dlqMessage.getPayloadJson()
				);

				dlqMessage.markGiveUp();
				continue;
			}

			dlqMessage.markProcessing();

			try {
				EstimateResultMessage message = objectMapper.readValue(
					dlqMessage.getPayloadJson(),
					EstimateResultMessage.class
				);

				rabbitTemplate.convertAndSend(
					RabbitMqConfig.EXCHANGE,
					RabbitMqConfig.RK_ESTIMATE_RESULT,
					message
				);

				dlqMessage.markResolved();

			} catch (Exception e) {
				log.warn("DLQ 메시지 재전송 실패: messageId={}, attempt={}, error={}",
					dlqMessage.getId(), dlqMessage.getAttemptCount(), e.getMessage());

				LocalDateTime next = now.plusSeconds(30L * (dlqMessage.getAttemptCount() + 1));
				dlqMessage.markPending(next);
			}
		}
	}
}
