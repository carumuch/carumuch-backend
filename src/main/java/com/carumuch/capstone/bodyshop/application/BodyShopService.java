package com.carumuch.capstone.bodyshop.application;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopInfoResDto;
import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopPageResDto;
import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopRegistrationReqDto;
import com.carumuch.capstone.bodyshop.presentation.dto.BodyShopUpdateReqDto;
import com.carumuch.capstone.common.exception.ForbiddenException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BodyShopService {
    private final BodyShopRepository bodyShopRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long register(BodyShopRegistrationReqDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(User.class));

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

    @Transactional
    public Long join(Long id, Long userId) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(User.class));

        user.registerMechanic();
        user.setBodyShop(bodyShop);
        return user.getId();
    }

    @Transactional
    public Long update(Long id, BodyShopUpdateReqDto requestDto, Long userId) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(User.class));

        if (user.getBodyShop().getId().equals(id)) {
            bodyShop.update(requestDto.getName(),
                    requestDto.getLocation(),
                    requestDto.getDescription(),
                    requestDto.getLink(),
                    requestDto.getPhoneNumber(),
                    requestDto.isPickupAvailability());
            return bodyShop.getId();
        } else {
            throw new ForbiddenException();
        }
    }

    public BodyShopInfoResDto findOne(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
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
