package com.carumuch.capstone.bodyshop.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.bodyshop.application.dto.BodyShopSearchCondition;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.bodyshop.domain.BodyShopRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BodyShopRepositoryImpl implements BodyShopRepository {

	private final JpaBodyShopRepository jpaBodyShopRepository;
	private final BodyShopQueryRepository bodyShopQueryRepository;

	@Override
	public BodyShop save(BodyShop bodyShop) {
		return jpaBodyShopRepository.save(bodyShop);
	}

	@Override
	public Optional<BodyShop> findById(Long id) {
		return jpaBodyShopRepository.findById(id);
	}

	@Override
	public Page<BodyShop> search(BodyShopSearchCondition condition, Pageable pageable) {
		return bodyShopQueryRepository.search(condition, pageable);
	}
}
