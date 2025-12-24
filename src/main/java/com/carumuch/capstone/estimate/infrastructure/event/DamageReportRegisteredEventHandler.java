package com.carumuch.capstone.estimate.infrastructure.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.carumuch.capstone.damage.domain.report.DamageReportRegisteredEvent;
import com.carumuch.capstone.estimate.infrastructure.mq.EstimateRequestPublisher;
import com.carumuch.capstone.estimate.infrastructure.mq.message.EstimateRequestMessage;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DamageReportRegisteredEventHandler {
	private final EstimateRequestPublisher estimateRequestPublisher;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onDamageReportRegistered(DamageReportRegisteredEvent event) {
		estimateRequestPublisher.publish(new EstimateRequestMessage(event.damageReportId(), event.brand(), event.imagePath()));
	}
}
