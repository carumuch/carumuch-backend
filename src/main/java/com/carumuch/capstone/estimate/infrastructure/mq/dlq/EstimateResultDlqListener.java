package com.carumuch.capstone.estimate.infrastructure.mq.dlq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
public class EstimateResultDlqListener {

	private final DlqMessageRepository dlqMessageRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	@RabbitListener(queues = RabbitMqConfig.ESTIMATE_RESULT_DLQ)
	public void onDlq(EstimateResultMessage message) {
		try {
			String payloadJson = objectMapper.writeValueAsString(message);
			dlqMessageRepository.save(
				new DlqMessage(RabbitMqConfig.ESTIMATE_RESULT_DLQ, payloadJson)
			);

		} catch (Exception e) {
			log.error("DLQ 메세지 저장 실패: message={}", message.toString());
		}
	}
}
