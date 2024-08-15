package com.carumuch.capstone.bodyshop.service;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.dto.BodyShopInfoResDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopPageResDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.bodyshop.repository.BodyShopRepository;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
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
}
