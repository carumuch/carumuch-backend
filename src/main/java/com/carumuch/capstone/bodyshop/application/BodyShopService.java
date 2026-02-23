package com.carumuch.capstone.bodyshop.application;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopListResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
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

        return bodyShopRepository.save(requestDto.toEntity(user)).getId();
    }

    public PagingResponse<BodyShopListResponse> searchKeyword(PagingRequest pagingRequest, String keyword) {
        Page<BodyShop> bodyShops = bodyShopRepository
                .findPageByNameLikeKeyword(keyword, PageRequest.of(pagingRequest.page(), pagingRequest.size(), Sort.by(pagingRequest.sort())));
		return PagingResponse.from(bodyShops.map(BodyShopListResponse::new));
    }

    @Transactional
    public Long update(Long id, UpdateBodyShopRequest requestDto, Long userId) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));

		// todo: 권한 확인이 필요합니다.
        // if (!user.getBodyShop().getId().equals(id)) {
		// 	throw new ForbiddenException();
        // }
		bodyShop.update(requestDto.name(),
			requestDto.location(),
			requestDto.description(),
			requestDto.link(),
			requestDto.phoneNumber(),
			requestDto.pickupAvailability());
		return bodyShop.getId();
    }

    public BodyShopInfoResponse findOne(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
        return new BodyShopInfoResponse(
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
