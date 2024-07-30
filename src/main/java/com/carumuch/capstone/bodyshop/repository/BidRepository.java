package com.carumuch.capstone.bodyshop.repository;

import com.carumuch.capstone.bodyshop.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
}
