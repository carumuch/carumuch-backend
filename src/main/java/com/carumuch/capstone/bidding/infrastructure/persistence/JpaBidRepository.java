package com.carumuch.capstone.bidding.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carumuch.capstone.bidding.domain.Bid;

public interface JpaBidRepository extends JpaRepository<Bid, Long> {

	@Query("""
		SELECT b
		FROM Bid b
		JOIN FETCH b.bodyShop
		WHERE b.id = :id
	""")
	Optional<Bid> findByIdWithBodyShop(@Param("id") Long id);

	@Query("""
		SELECT b
		FROM Bid b
		JOIN FETCH b.estimate
		JOIN FETCH b.bodyShop
		WHERE b.id = :id
	""")
	Optional<Bid> findByIdWithBodyShopAndEstimate(@Param("id") Long id);

	@Query("""
		SELECT b
		FROM Bid b
		JOIN FETCH b.bodyShop
		WHERE b.estimate.id = :estimateId
	""")
	Page<Bid> findPageByEstimateIdWithBodyShop(@Param("estimateId") Long estimateId, Pageable pageable);
}
