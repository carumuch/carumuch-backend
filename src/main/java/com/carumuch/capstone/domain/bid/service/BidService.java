package com.carumuch.capstone.domain.bid.service;

import com.carumuch.capstone.domain.bid.model.Bid;
import com.carumuch.capstone.domain.bodyshop.model.BodyShop;
import com.carumuch.capstone.domain.bid.model.type.BidStatus;
import com.carumuch.capstone.domain.bid.dto.BidPageResDto;
import com.carumuch.capstone.domain.bid.dto.BidResDto;
import com.carumuch.capstone.domain.bid.repository.BidRepository;
import com.carumuch.capstone.global.exception.CustomException;
import com.carumuch.capstone.domain.estimate.model.Estimate;
import com.carumuch.capstone.domain.estimate.model.type.EstimateStatus;
import com.carumuch.capstone.domain.estimate.repository.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carumuch.capstone.global.exception.ErrorCode.*;

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
        bodyShop.acceptCount();

        // 견적서를 공개에서 비공개로 전환
        Estimate estimate = estimateRepository.findById(bid.getEstimate().getId())
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND));
        estimate.update(EstimateStatus.PRIVATE);
        return bid.getId();
    }
}
