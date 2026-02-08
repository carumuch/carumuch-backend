package com.carumuch.capstone.estimate.infrastructure.mq;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.carumuch.capstone.damage.domain.report.DamageReport;
import com.carumuch.capstone.damage.domain.report.DamageReportRepository;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.infrastructure.mq.config.RabbitMqConfig;
import com.carumuch.capstone.estimate.infrastructure.mq.message.EstimateResultMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EstimateResultListener {
	private static final String INVALID_MESSAGE = RabbitMqConfig.ESTIMATE_RESULT_Q + ": 유효하지 않은 메시지입니다.";

	private final EstimateRepository estimateRepository;
	private final DamageReportRepository damageReportRepository;

	@Transactional
	@RabbitListener(queues = RabbitMqConfig.ESTIMATE_RESULT_Q)
	public void onResult(EstimateResultMessage message) {
		if (message == null || message.damageReportId() == null) {
			throw new AmqpRejectAndDontRequeueException(INVALID_MESSAGE);
		}

		DamageReport damageReport = damageReportRepository.findById(message.damageReportId())
			.orElseThrow(() -> new AmqpRejectAndDontRequeueException(INVALID_MESSAGE));

		estimateRepository.save(
			new Estimate(
				message.repairCost(),
				message.repairParts(),
				EstimateStatus.OPEN,
				message.imagePath(),
				damageReport
			)
		);
	}
}
