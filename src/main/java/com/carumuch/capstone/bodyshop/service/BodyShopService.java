package com.carumuch.capstone.bodyshop.service;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.dto.BodyShopPageResDto;
import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
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
    public Page<BodyShopPageResDto> searchKeyword(int id, String keyword) {
        Page<BodyShop> page = bodyShopRepository
                .findPageByNameLikeKeyword(keyword, PageRequest.of(id - 1, 10, Sort.by(Sort.Direction.DESC,"createDate")));
        return page.map(bodyShop -> BodyShopPageResDto.builder()
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
}
