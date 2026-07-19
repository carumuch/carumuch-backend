package com.carumuch.capstone.bidding.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carumuch.capstone.bidding.domain.Bid;

public interface JpaBidRepository extends JpaRepository<Bid, Long> {

	@Query("select b from Bid b left join fetch b.bodyShop where b.id = :id")
	Optional<Bid> findByIdWithBodyShop(@Param("id") Long id);

	@Query("select b from Bid b left join fetch b.estimate where b.id = :id")
	Optional<Bid> findByIdWithEstimate(@Param("id") Long id);

	@Query("select b from Bid b where b.estimate.id = :estimateId")
	Page<Bid> findPageByEstimateId(@Param("estimateId") Long estimateId, Pageable pageable);
}
