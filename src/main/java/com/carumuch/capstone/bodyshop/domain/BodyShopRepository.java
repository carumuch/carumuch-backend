package com.carumuch.capstone.bodyshop.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carumuch.capstone.bodyshop.application.dto.BodyShopSearchCondition;

public interface BodyShopRepository {

	BodyShop save(BodyShop bodyShop);

	Optional<BodyShop> findById(Long id);

	Page<BodyShop> search(BodyShopSearchCondition condition, Pageable pageable);
}
