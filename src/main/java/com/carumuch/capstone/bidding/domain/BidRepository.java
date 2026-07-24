package com.carumuch.capstone.bidding.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BidRepository {
    Bid save(Bid bid);

    Bid findById(Long id);
    Bid findByIdWithBodyShop(Long id);
    Bid findByIdWithBodyShopAndEstimate(Long id);
    Page<Bid> findPageByEstimateIdWithBodyShop(Long estimateId, Pageable pageable);
}
