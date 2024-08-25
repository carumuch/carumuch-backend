package com.carumuch.capstone.bodyshop.repository;

import com.carumuch.capstone.bodyshop.domain.Bid;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("select b from Bid b left join fetch b.bodyShop where b.id = :id")
    Optional<Bid> findByIdWithBodyShop(@Param("id") Long id);
}
