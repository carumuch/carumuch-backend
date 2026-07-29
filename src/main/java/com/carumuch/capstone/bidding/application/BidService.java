package com.carumuch.capstone.bidding.application;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bidding.presentation.dto.response.BidListResponse;
import com.carumuch.capstone.bidding.presentation.dto.response.BidInfoResponse;
import com.carumuch.capstone.bidding.domain.BidRepository;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;

    public PagingResponse<BidListResponse> getBids(Long estimateId, PagingRequest pagingRequest) {
        Page<Bid> bids = bidRepository.findPageByEstimateIdWithBodyShop(estimateId, pagingRequest.toPageRequest());

        return PagingResponse.from(bids.map(BidListResponse::new));
    }

    public BidInfoResponse detailBid(Long id) {
        return new BidInfoResponse(bidRepository.findByIdWithBodyShop(id));
    }

    @Transactional
    public void acceptBid(Long id, Long userId) {
        Bid bid = bidRepository.findByIdWithBodyShopAndEstimate(id);

        bid.validateEstimateRequester(userId);

        bid.accept();
        bid.getBodyShop().increaseAcceptCount();
        bid.getEstimate().closeBidding();
    }
}
