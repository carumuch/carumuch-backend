package com.carumuch.capstone.bodyshop.repository;

import com.carumuch.capstone.bodyshop.domain.BodyShop;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BodyShopRepository extends JpaRepository<BodyShop, Long> {

    @Query("select b from BodyShop b where b.name like %:keyword%")
    Page<BodyShop> findPageByNameLikeKeyword(@Param("keyword") String keyword, Pageable pageable);
}
