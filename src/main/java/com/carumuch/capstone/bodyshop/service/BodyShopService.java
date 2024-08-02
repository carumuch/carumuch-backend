package com.carumuch.capstone.bodyshop.service;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.repository.BodyShopRepository;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
