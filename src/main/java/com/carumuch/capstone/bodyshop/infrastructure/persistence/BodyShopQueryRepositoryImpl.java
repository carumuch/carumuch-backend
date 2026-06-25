package com.carumuch.capstone.bodyshop.infrastructure.persistence;

import static com.carumuch.capstone.bodyshop.domain.QBodyShop.bodyShop;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.bodyshop.application.dto.BodyShopSearchCondition;
import com.carumuch.capstone.bodyshop.domain.BodyShop;
import com.carumuch.capstone.common.infrastructure.querydsl.DynamicBooleanBuilder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BodyShopQueryRepositoryImpl implements BodyShopQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<BodyShop> search(BodyShopSearchCondition condition, Pageable pageable) {
		BooleanBuilder predicate = DynamicBooleanBuilder.builder()
			.and(() -> bodyShop.name.contains(condition.keyword()))
			.and(() -> bodyShop.location.sido.eq(condition.sido()))
			.and(() -> bodyShop.location.siqungu.eq(condition.sigungu()))
			.and(() -> bodyShop.pickupAvailable.eq(condition.pickupAvailable()))
			.build();

		List<BodyShop> content = jpaQueryFactory
			.selectFrom(bodyShop)
			.where(predicate)
			.orderBy(getOrderSpecifier(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(bodyShop.count())
			.from(bodyShop)
			.where(predicate);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
		Sort.Order order = pageable.getSort().stream()
			.findFirst()
			.orElse(Sort.Order.asc("createDate"));

		return switch (order.getProperty()) {
			case "name" -> order.isAscending() ? bodyShop.name.asc() : bodyShop.name.desc();
			case "acceptCount" -> order.isAscending() ? bodyShop.acceptCount.asc() : bodyShop.acceptCount.desc();
			case "pickupAvailable" -> order.isAscending() ? bodyShop.pickupAvailable.asc() : bodyShop.pickupAvailable.desc();
			default -> order.isAscending() ? bodyShop.createDate.asc() : bodyShop.createDate.desc();
		};
	}
}
