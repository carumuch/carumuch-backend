package com.carumuch.capstone.bodyshop.application;

import com.carumuch.capstone.bodyshop.application.dto.BodyShopSearchCondition;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;
import com.carumuch.capstone.bodyshop.domain.PhoneNumber;
import com.carumuch.capstone.bodyshop.presentation.dto.request.SearchBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopListResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.common.exception.ForbiddenException;
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

		BodyShop bodyShop = bodyShopRepository.save(requestDto.toEntity(userId));
		user.assignBodyShop(bodyShop);

		return bodyShop.getId();
    }

    @Transactional
    public void update(Long id, UpdateBodyShopRequest requestDto, Long userId) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));

        if (!bodyShop.canAccess(userId)) {
			throw new ForbiddenException();
        }
		bodyShop.update(requestDto.name(),
			requestDto.locationRequest().toLocation(),
			requestDto.description(),
			requestDto.link(),
			new PhoneNumber(requestDto.phoneNumber()),
			requestDto.pickupAvailable());
    }

    public BodyShopInfoResponse info(Long id) {
        BodyShop bodyShop = bodyShopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BodyShop.class));
        return BodyShopInfoResponse.from(bodyShop);
    }

	public PagingResponse<BodyShopListResponse> search(SearchBodyShopRequest request, PagingRequest pagingRequest) {
		Page<BodyShop> bodyShops = bodyShopRepository
			.search(
				BodyShopSearchCondition.from(request), pagingRequest.toPageRequest()
			);
		return PagingResponse.from(bodyShops.map(BodyShopListResponse::new));
	}
}
