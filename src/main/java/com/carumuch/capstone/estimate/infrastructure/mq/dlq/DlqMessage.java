package com.carumuch.capstone.estimate.infrastructure.mq.dlq;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dlq_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DlqMessage {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String originalQueue;

	@Lob @Column(nullable = false)
	private String payloadJson;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private DlqStatus status;

	@Column(nullable = false)
	private int attemptCount;

	private LocalDateTime nextAttemptAt;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
		if (this.status == null) this.status = DlqStatus.PENDING;
		if (this.nextAttemptAt == null) this.nextAttemptAt = this.createdAt;
	}

	@PreUpdate
	void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public DlqMessage(String originalQueue, String payloadJson) {
		this.originalQueue = originalQueue;
		this.payloadJson = payloadJson;
		this.status = DlqStatus.PENDING;
		this.attemptCount = 0;
		this.nextAttemptAt = LocalDateTime.now();
	}

	public void markProcessing() {
		this.status = DlqStatus.PROCESSING;
	}

	public void markResolved() {
		this.status = DlqStatus.RESOLVED;
	}

	public void markGiveUp() {
		this.status = DlqStatus.GIVE_UP;
	}

	public void markPending(LocalDateTime nextAttemptAt) {
		this.status = DlqStatus.PENDING;
		this.attemptCount++;
		this.nextAttemptAt = nextAttemptAt;
	}
}

