package com.carumuch.capstone.bodyshop.service;

import com.carumuch.capstone.bodyshop.domain.Bid;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.type.BidStatus;
import com.carumuch.capstone.bodyshop.dto.*;
import com.carumuch.capstone.bodyshop.repository.BidRepository;
import com.carumuch.capstone.bodyshop.repository.BodyShopRepository;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.dto.EstimateDetailResDto;
import com.carumuch.capstone.vehicle.dto.EstimateSearchReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateSearchResDto;
import com.carumuch.capstone.vehicle.repository.EstimateRepository;
import com.carumuch.capstone.vehicle.repository.custom.EstimateRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BodyShopService {
    private final BodyShopRepository bodyShopRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final BidRepository bidRepository;
    private final EstimateRepositoryCustom estimateRepositoryCustom;

    /**
     * 공업사 직업 여부
     */
    public void validateMechanicUser() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userRepository.findLoginUserByLoginId(loginId).isMechanic()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * 해당 공업사의 입찰 건이 맞는지 여부
     */
    public void validationBodyShopBid(Long bodyShopId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLoginIdWithBodyShop(loginId);
        if (user.getBodyShop() == null) throw new CustomException(ErrorCode.ACCESS_DENIED); // 공업사 유저가 아닐 경우
        if (!user.getBodyShop().getId().equals(bodyShopId)) throw new CustomException(ErrorCode.ACCESS_DENIED); // 입찰건 주인이 해당 공업사가 아닐 경우
    }

    /**
     * 공업사 가입
     */
    @Transactional
    public Long register(BodyShopRegistrationReqDto requestDto) {
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        /* 사용자를 공업사 직원으로 등록 */
        user.registerMechanic();

        return bodyShopRepository.save(BodyShop.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .location(requestDto.getLocation())
                .link(requestDto.getLink())
                .phoneNumber(requestDto.getPhoneNumber())
                .pickupAvailability(requestDto.isPickupAvailability())
                .user(user)
                .build()).getId();
    }

    /**
     * Select: 공업사 키워드 검색
     * 공업사 가입 전 해당 공업사 검색
     */
    public Page<BodyShopPageResDto> searchKeyword(int page, String keyword) {
        Page<BodyShop> bodyShopPage = bodyShopRepository
                .findPageByNameLikeKeyword(keyword, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC,"createDate")));
        return bodyShopPage.map(bodyShop -> BodyShopPageResDto.builder()
                        .id(bodyShop.getId())
                        .name(bodyShop.getName())
                        .acceptCount(bodyShop.getAcceptCount())
                        .pickupAvailability(bodyShop.isPickupAvailability())
                        .location(bodyShop.getLocation())
                        .build());
    }

    /**
     * Update: 기존 공업사에 직원으로 가입
     */
    @Transactional
    public Long join(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        User user = userRepository
                .findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        user.registerMechanic(); // 사용자를 공업사 직원으로 등록
        user.setBodyShop(bodyShop); // 해당 공업사로 등록
        return user.getId();
    }

    /**
     * Update: 공업사 정보 수정
     */
    @Transactional
    public Long update(Long id, BodyShopUpdateReqDto requestDto) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        User user = userRepository
                .findByLoginIdWithBodyShop(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.getBodyShop().getId().equals(id)) {
            bodyShop.update(requestDto.getName(),
                    requestDto.getLocation(),
                    requestDto.getDescription(),
                    requestDto.getLink(),
                    requestDto.getPhoneNumber(),
                    requestDto.isPickupAvailability());
            return bodyShop.getId();
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    /**
     * Update: 다른 공업사로 변경
     */
    @Transactional
    public Long transfer(Long id) {
        validateMechanicUser();
        User user = userRepository
                .findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        user.setBodyShop(bodyShop);
        return user.getId();
    }

    /**
     * Select: 공업사 상세 조회
     */
    public BodyShopInfoResDto findOne(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return BodyShopInfoResDto.builder()
                .id(bodyShop.getId())
                .name(bodyShop.getName())
                .description(bodyShop.getDescription())
                .phoneNumber(bodyShop.getPhoneNumber())
                .link(bodyShop.getLink())
                .acceptCount(bodyShop.getAcceptCount())
                .pickupAvailability(bodyShop.isPickupAvailability())
                .location(bodyShop.getLocation())
                .build();
    }

    /**
     * Select: 공업사 측 사용자 견적 상세 조회
     */
    public EstimateDetailResDto estimateDetail(Long id) {

        /* 공업사 측인지 확인 */
        validateMechanicUser();

        Estimate estimate = estimateRepository.findByIdWithVehicle(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return EstimateDetailResDto.builder()
                .estimate(estimate)
                .build();
    }

    /**
     * Select: 공업사 측 사용자 견적 목록 상세 조회
     */
    public Page<EstimateSearchResDto> searchEstimateList(EstimateSearchReqDto estimateSearchReqDto) {
        /* 공업사 측인지 확인 */
        validateMechanicUser();

        return estimateRepositoryCustom.searchPage(estimateSearchReqDto,
                PageRequest.of(estimateSearchReqDto.getPage() != null ? estimateSearchReqDto.getPage() - 1 : 0, 10)); // 1페이지를 위한 -1 수행
    }

    /**
     * Create: 공업사 측 특정 견적서에 대해 입찰 신청
     */
    @Transactional
    public Long createBid(Long estimateId, BodyShopBidCreateReqDto bodyShopBidCreateReqDto) {

        /* 공업사 측인지 확인 */
        validateMechanicUser();

        User user = userRepository
                .findByLoginIdWithBodyShop(SecurityContextHolder.getContext().getAuthentication().getName());

        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return bidRepository.save(Bid.builder()
                        .bidStatus(BidStatus.WAITING)
                        .cost(bodyShopBidCreateReqDto.getCost())
                        .repairMethod(bodyShopBidCreateReqDto.getRepairMethod())
                        .bodyShop(user.getBodyShop())
                        .estimate(estimate)
                .build()).getId();
    }

    /**
     * Select: 공업사 측 입찰 상세 조회
     */
    public BodyShopBidDetailResDto bidDetail(Long bidId) {

        Bid bid = bidRepository.findByIdWithBodyShop(bidId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        /* 해당 공업사 입찰 건 이 맞는지 확인 */
        validationBodyShopBid(bid.getBodyShop().getId());

        return BodyShopBidDetailResDto.builder()
                .id(bid.getId())
                .cost(bid.getCost())
                .repairMethod(bid.getRepairMethod())
                .bidStatus(bid.getBidStatus().getKey())
                .createDate(bid.getCreateDate())
                .build();
    }

    /**
     * Update: 공업사 측 입찰 정보 수정
     */
    @Transactional
    public Long updateBid(Long bidId, BodyShopBidUpdateReqDto bodyShopBidUpdateReqDto) {
        Bid bid = bidRepository.findByIdWithBodyShop(bidId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        /* 해당 공업사 입찰 건 이 맞는지 확인 */
        validationBodyShopBid(bid.getBodyShop().getId());

        bid.update(bodyShopBidUpdateReqDto.getCost(),bodyShopBidUpdateReqDto.getRepairMethod());
        return bid.getId();
    }

    /**
     * Delete: 공업사 측 입찰 취소
     */
    @Transactional
    public void cancelBid(Long bidId) {
        Bid bid = bidRepository.findByIdWithBodyShop(bidId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        /* 해당 공업사 입찰 건 이 맞는지 확인 */
        validationBodyShopBid(bid.getBodyShop().getId());

        bidRepository.deleteById(bid.getId());
    }
}
