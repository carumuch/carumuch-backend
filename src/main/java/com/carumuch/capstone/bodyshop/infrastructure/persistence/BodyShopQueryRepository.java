package com.carumuch.capstone.bodyshop.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.carumuch.capstone.bodyshop.application.dto.BodyShopSearchCondition;
import com.carumuch.capstone.bodyshop.domain.BodyShop;

public interface BodyShopQueryRepository {
	Page<BodyShop> search(BodyShopSearchCondition condition, Pageable pageable);
}
