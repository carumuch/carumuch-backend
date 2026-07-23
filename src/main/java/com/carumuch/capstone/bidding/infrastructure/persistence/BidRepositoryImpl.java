package com.carumuch.capstone.bidding.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bidding.domain.BidRepository;
import com.carumuch.capstone.common.exception.NotFoundException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepository {

	private final JpaBidRepository jpaBidRepository;

	@Override
	public Bid findById(Long id) {
		return jpaBidRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(Bid.class));
	}

	@Override
	public Bid save(Bid bid) {
		return jpaBidRepository.save(bid);
	}

	@Override
	public Bid findByIdWithBodyShop(Long id) {
		return jpaBidRepository.findByIdWithBodyShop(id)
			.orElseThrow(() -> new NotFoundException(Bid.class));
	}

	@Override
	public Bid findByIdWithBodyShopAndEstimate(Long id) {
		return jpaBidRepository.findByIdWithBodyShopAndEstimate(id)
			.orElseThrow(() -> new NotFoundException(Bid.class));
	}

	@Override
	public Page<Bid> findPageByEstimateId(Long estimateId, Pageable pageable) {
		return jpaBidRepository.findPageByEstimateId(estimateId, pageable);
	}
}
