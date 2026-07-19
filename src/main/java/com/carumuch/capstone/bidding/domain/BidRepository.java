package com.carumuch.capstone.bidding.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BidRepository {
    Bid findById(Long id);
    Bid save(Bid bid);
    Bid findByIdWithBodyShop(Long id);
    Bid findByIdWithEstimate(Long id);
    Page<Bid> findPageByEstimateId(Long estimateId, Pageable pageable);
}
