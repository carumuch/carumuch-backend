package com.carumuch.capstone.estimate.infrastructure.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.carumuch.capstone.estimate.infrastructure.mq.config.RabbitMqConfig;
import com.carumuch.capstone.estimate.infrastructure.mq.message.EstimateRequestMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstimateRequestPublisher {

	private final RabbitTemplate rabbitTemplate;

	public void publish(EstimateRequestMessage message) {
		rabbitTemplate.convertAndSend(
			RabbitMqConfig.EXCHANGE,
			RabbitMqConfig.RK_ESTIMATE_REQUEST,
			message
		);
	}
}
