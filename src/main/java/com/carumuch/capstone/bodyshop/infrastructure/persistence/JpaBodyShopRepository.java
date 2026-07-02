package com.carumuch.capstone.bodyshop.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carumuch.capstone.bodyshop.domain.BodyShop;

public interface JpaBodyShopRepository extends JpaRepository<BodyShop, Long> {
}
