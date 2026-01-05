package com.carumuch.capstone.estimate.infrastructure.mq.dlq;

public enum DlqStatus {
	PENDING, PROCESSING, RESOLVED, GIVE_UP
}
