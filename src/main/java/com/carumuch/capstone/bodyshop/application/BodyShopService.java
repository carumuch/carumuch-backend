package com.carumuch.capstone.bodyshop.application;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResDto;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopListResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
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
    public Long register(RegisterBodyShopRequest requestDto, Long userId) {
        User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(User.class));

        user.registerMechanic();

        return bodyShopRepository.save(BodyShop.builder()
                .name(requestDto.name())
                .description(requestDto.description())
                .location(requestDto.location())
                .link(requestDto.link())
                .phoneNumber(requestDto.phoneNumber())
                .pickupAvailability(requestDto.pickupAvailability())
                .user(user)
                .build()).getId();
    }

    public Page<BodyShopListResponse> searchKeyword(int page, String keyword) {
        Page<BodyShop> bodyShopPage = bodyShopRepository
                .findPageByNameLikeKeyword(keyword, PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC,"createDate")));
        return bodyShopPage.map(bodyShop -> new BodyShopListResponse(
			bodyShop.getId(),
			bodyShop.getName(),
			bodyShop.getAcceptCount(),
			bodyShop.isPickupAvailability(),
			bodyShop.getLocation()
			)
		);
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
    public Long update(Long id, UpdateBodyShopRequest requestDto, Long userId) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(User.class));

        if (user.getBodyShop().getId().equals(id)) {
            bodyShop.update(requestDto.name(),
                    requestDto.location(),
                    requestDto.description(),
                    requestDto.link(),
                    requestDto.phoneNumber(),
                    requestDto.pickupAvailability());
            return bodyShop.getId();
        } else {
            throw new ForbiddenException();
        }
    }

    public BodyShopInfoResDto findOne(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
        return new BodyShopInfoResDto(
			bodyShop.getId(),
			bodyShop.getName(),
			bodyShop.getLocation(),
			bodyShop.getDescription(),
			bodyShop.getPhoneNumber(),
			bodyShop.getLink(),
			bodyShop.getAcceptCount(),
			bodyShop.isPickupAvailability()
		);
    }
}
