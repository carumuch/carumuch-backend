package com.carumuch.capstone.domain.bid.repository;

import com.carumuch.capstone.domain.bid.model.Bid;
import com.carumuch.capstone.domain.bid.model.type.BidStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("select b from Bid b left join fetch b.bodyShop where b.id = :id")
    Optional<Bid> findByIdWithBodyShop(@Param("id") Long id);

    @Query("select b from Bid b left join fetch b.estimate where b.bodyShop.id =:id")
    Page<Bid> findPageByBodyShopId(@Param("id") Long id, Pageable pageable);

    @Query("select (count(b) > 0) from Bid b where b.bodyShop.id = :bodyShopId and b.estimate.id = :estimateId")
    boolean existsByEstimateId(@Param("bodyShopId") Long bodyShopId, @Param("estimateId") Long estimateId);

    @Query(""" 
            select b from Bid b where b.estimate.id = :estimateId
            """)
    Page<Bid> findPageByEstimateId(@Param("estimateId") Long estimateId, Pageable pageable);

    @Query("""
            select b from Bid b left join fetch b.estimate where b.id = :id
            """)
    Optional<Bid> findByIdWithEstimate(@Param("id") Long id);

    @Query("""
        select (count(b) > 0) from Bid b where b.estimate.id = :estimateId and b.bidStatus = :bidStatus
        """)
    boolean existsBidByEstimateId(@Param("estimateId") Long estimateId, @Param("bidStatus") BidStatus bidStatus);
}
