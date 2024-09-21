package com.carumuch.capstone.bodyshop.service;

import com.carumuch.capstone.bodyshop.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.type.BidStatus;
import com.carumuch.capstone.bodyshop.dto.bid.BidPageResDto;
import com.carumuch.capstone.bodyshop.dto.bid.BidResDto;
import com.carumuch.capstone.bodyshop.repository.BidRepository;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.type.EstimateStatus;
import com.carumuch.capstone.vehicle.repository.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.carumuch.capstone.global.common.ErrorCode.*;

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
    public Long updateBisStatus(Long id, String status) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        Bid bid = bidRepository.findByIdAndCreateByWithEstimate(id, loginId)
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND));
        // 이미 매칭 된 견적인지
        if (bidRepository.existsBidByEstimateId(bid.getEstimate().getId(), BidStatus.ACCEPTED)) {
            throw new CustomException(DUPLICATE_RESOURCE);
        };

        // 상태 업데이트
        bid.updateStatus(BidStatus.valueOf(status));

        // 견적서를 공개에서 비공개로 전환
        Estimate estimate = estimateRepository.findById(bid.getEstimate().getId())
                .orElseThrow(() -> new CustomException(RESOURCE_NOT_FOUND));
        estimate.update(EstimateStatus.PRIVATE);
        return bid.getId();
    }
}
