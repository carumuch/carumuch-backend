package com.carumuch.capstone.estimate.infrastructure.mq.dlq;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DlqMessageRepository extends JpaRepository<DlqMessage, Long> {

	@Query(
		"SELECT d FROM DlqMessage d "
			+ "WHERE d.status = :status "
			+ "AND d.nextAttemptAt IS NOT NULL "
			+ "AND d.nextAttemptAt <= :now "
			+ "ORDER BY d.nextAttemptAt ASC"
	)
	List<DlqMessage> findRetryMessages(
		@Param("status") DlqStatus status,
		@Param("now") LocalDateTime now,
		Pageable pageable
	);
}
