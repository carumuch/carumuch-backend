package com.carumuch.capstone.estimate.infrastructure.persistence;

import static com.carumuch.capstone.estimate.domain.QEstimate.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.report.QDamageReport;
import com.carumuch.capstone.damage.domain.vehicle.QVehicle;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.domain.QEstimate;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EstimateQueryRepositoryImpl implements EstimateQueryRepository {

	private static final String POPULAR_SORT = "POPULAR";
	private static final String DEFAULT_SORT = "DEFAULT";
	private static final OrderSpecifier<?>[] ORDER_POPULAR = {QEstimate.estimate.applicantCount.desc()};
	private static final OrderSpecifier<?>[] ORDER_DEFAULT = {QEstimate.estimate.createDate.desc()};

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Estimate> searchEstimates(EstimateSearchCondition condition, Pageable pageable) {

		QDamageReport dr = QDamageReport.damageReport;
		QVehicle v = QVehicle.vehicle;
		BooleanBuilder predicate = buildPredicate(condition, dr, v);

		List<Estimate> content = jpaQueryFactory
			.select(estimate)
			.from(estimate)
			.join(estimate.damageReport, dr).fetchJoin()
			.join(dr.vehicle, v).fetchJoin()
			.where(predicate)
			.orderBy(getOrderSpecifier(pageable))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = jpaQueryFactory
			.select(estimate.count())
			.from(estimate)
			.join(estimate.damageReport, dr)
			.join(dr.vehicle, v)
			.where(predicate);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	private BooleanBuilder buildPredicate(EstimateSearchCondition condition, QDamageReport dr, QVehicle v) {
		return DynamicBooleanBuilder.builder()
			.and(() -> estimate.estimateStatus.eq(EstimateStatus.OPEN))
			.and(() -> estimate.repairCost.loe(condition.maxRepairCost()))
			.and(() -> estimate.repairCost.goe(condition.minRepairCost()))
			.and(() -> dr.isPickupRequired.eq(condition.isPickupRequired()))
			.and(() -> dr.preferredRepairRegion.sido.eq(condition.sido()))
			.and(() -> dr.preferredRepairRegion.sigungu.eq(condition.sigungu()))
			.and(() -> v.modelName.eq(condition.modelName()))
			.and(() -> v.modelYear.eq(condition.modelYear()))
			.and(() -> v.brand.eq(condition.brand()))
			.build();
	}

	private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable) {
		String sort = pageable.getSort().stream()
			.findFirst()
			.map(Sort.Order::getProperty)
			.orElse(DEFAULT_SORT);

		return switch (sort) {
			case POPULAR_SORT -> ORDER_POPULAR;
			default -> ORDER_DEFAULT;
		};
	}
}
