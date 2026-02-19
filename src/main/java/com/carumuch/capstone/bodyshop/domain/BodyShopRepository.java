package com.carumuch.capstone.bodyshop.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BodyShopRepository {

	BodyShop save(BodyShop bodyShop);

	Optional<BodyShop> findById(Long id);

	Page<BodyShop> findPageByNameLikeKeyword(String keyword, Pageable pageable);
}
