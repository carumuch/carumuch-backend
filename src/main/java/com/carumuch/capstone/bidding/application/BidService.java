package com.carumuch.capstone.bidding.application;

import com.carumuch.capstone.bidding.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bidding.domain.BidStatus;
import com.carumuch.capstone.bidding.presentation.dto.response.BidPageResDto;
import com.carumuch.capstone.bidding.presentation.dto.response.BidResDto;
import com.carumuch.capstone.bidding.domain.BidRepository;
import com.carumuch.capstone.common.legacy.exception.CustomException;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carumuch.capstone.common.legacy.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final EstimateRepository estimateRepository;

    public Page<BidPageResDto> findPageByEstimateId(int page, Long estimateId) {
        Page<Bid> bidPage = bidRepository
                .findPageByEstimateId(estimateId, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createDate")));
        return bidPage.map(bid -> BidPageResDto.builder()
                .id(bid.getId())
                .cost(bid.getCost())
                .bidStatus(bid.getBidStatus().getKey())
                .build());
    }

    public BidResDto detailBid(Long id) {
        return new BidResDto(bidRepository.findByIdWithBodyShop(id)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND)));
    }

    @Transactional
    public Long updateBidStatus(Long id, String status) {
        Bid bid = bidRepository.findByIdWithEstimate(id)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND));
        // 이미 매칭 된 견적인지
        if (bidRepository.existsBidByEstimateId(bid.getEstimate().getId(), BidStatus.ACCEPTED)) {
            throw new CustomException(BID_ALREADY_COMPLETED);
        };

        // 상태 업데이트
        bid.updateStatus(BidStatus.valueOf(status));

        // 체결 공업사 입찰 횟수 증가
        BodyShop bodyShop = bidRepository.findByIdWithBodyShop(id)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND)).getBodyShop();
        bodyShop.increaseAcceptCount();

        // 견적서를 공개에서 비공개로 전환
        Estimate estimate = estimateRepository.findById(bid.getEstimate().getId())
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND));
        estimate.changeStatus(EstimateStatus.PRIVATE);
        return bid.getId();
    }
}
