package com.carumuch.capstone.estimate.infrastructure.persistence;

import static com.carumuch.capstone.estimate.domain.QEstimate.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.carumuch.capstone.damage.domain.report.QDamageReport;
import com.carumuch.capstone.damage.domain.vehicle.QVehicle;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollQuery;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EstimateQueryRepositoryImpl implements EstimateQueryRepository {

	private static final int DEFAULT_MIN_REPAIR_COST = 0;
	private static final int DEFAULT_MAX_REPAIR_COST = 999_999;
	private static final OrderSpecifier<?>[] ORDER_BY_LATEST = {estimate.createDate.desc(), estimate.id.desc()};

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public EstimateScrollSlice<Estimate> searchEstimates(EstimateSearchCondition condition, EstimateScrollQuery scrollQuery) {
		QDamageReport dr = QDamageReport.damageReport;
		QVehicle v = QVehicle.vehicle;

		List<Long> estimateIds = jpaQueryFactory
			.select(estimate.id)
			.from(estimate)
			.join(estimate.damageReport, dr)
			.join(dr.vehicle, v)
			.where(buildPredicates(condition, scrollQuery, dr, v))
			.orderBy(ORDER_BY_LATEST)
			.limit(scrollQuery.limitSize())
			.fetch();

		boolean hasNext = estimateIds.size() > scrollQuery.size();
		List<Long> pagedEstimateIds = hasNext
			? estimateIds.subList(0, scrollQuery.size())
			: estimateIds;

		List<Estimate> content = pagedEstimateIds.isEmpty()
			? Collections.emptyList()
			: jpaQueryFactory
			  .select(estimate)
			  .from(estimate)
			  .join(estimate.damageReport, dr).fetchJoin()
			  .join(dr.vehicle, v).fetchJoin()
			  .where(estimate.id.in(pagedEstimateIds))
			  .orderBy(ORDER_BY_LATEST)
			  .fetch();

		return new EstimateScrollSlice<>(
			content,
			hasNext,
			hasNext ? resolveNextCursorId(content) : null,
			hasNext ? resolveNextCursorCreatedAt(content) : null
		);
	}

	private BooleanExpression[] buildPredicates(
		EstimateSearchCondition condition,
		EstimateScrollQuery scrollQuery,
		QDamageReport dr,
		QVehicle v
	) {
		return new BooleanExpression[] {
			estimate.estimateStatus.eq(EstimateStatus.OPEN),
			cursorPredicate(scrollQuery),
			repairCostLoe(condition),
			repairCostGoe(condition),
			pickupRequiredPredicate(condition, dr),
			sidoPredicate(condition, dr),
			sigunguPredicate(condition, dr),
			modelNamePredicate(condition, v),
			modelYearPredicate(condition, v),
			brandPredicate(condition, v)
		};
	}

	private BooleanExpression cursorPredicate(EstimateScrollQuery scrollQuery) {
		if (!scrollQuery.hasCursor()) {
			return null;
		}

		return estimate.createDate.lt(scrollQuery.cursorCreatedAt())
			.or(estimate.createDate.eq(scrollQuery.cursorCreatedAt())
				.and(estimate.id.lt(scrollQuery.cursorId())));
	}

	private BooleanExpression repairCostLoe(EstimateSearchCondition condition) {
		return estimate.repairCost.loe(condition.maxRepairCost() != null ? condition.maxRepairCost() : DEFAULT_MAX_REPAIR_COST);
	}

	private BooleanExpression repairCostGoe(EstimateSearchCondition condition) {
		return estimate.repairCost.goe(condition.minRepairCost() != null ? condition.minRepairCost() : DEFAULT_MIN_REPAIR_COST);
	}

	private BooleanExpression pickupRequiredPredicate(EstimateSearchCondition condition, QDamageReport dr) {
		return condition.isPickupRequired() != null
			? dr.isPickupRequired.eq(condition.isPickupRequired())
			: dr.isPickupRequired.in(true, false);
	}

	private BooleanExpression sidoPredicate(EstimateSearchCondition condition, QDamageReport dr) {
		return condition.sido() != null ? dr.preferredRepairRegion.sido.eq(condition.sido()) : null;
	}

	private BooleanExpression sigunguPredicate(EstimateSearchCondition condition, QDamageReport dr) {
		return condition.sigungu() != null ? dr.preferredRepairRegion.sigungu.eq(condition.sigungu()) : null;
	}

	private BooleanExpression modelNamePredicate(EstimateSearchCondition condition, QVehicle v) {
		return condition.modelName() != null ? v.modelName.eq(condition.modelName()) : null;
	}

	private BooleanExpression modelYearPredicate(EstimateSearchCondition condition, QVehicle v) {
		return condition.modelYear() != null ? v.modelYear.eq(condition.modelYear()) : null;
	}

	private BooleanExpression brandPredicate(EstimateSearchCondition condition, QVehicle v) {
		return condition.brand() != null ? v.brand.eq(condition.brand()) : null;
	}

	private Long resolveNextCursorId(List<Estimate> content) {
		return content.isEmpty() ? null : content.get(content.size() - 1).getId();
	}

	private LocalDateTime resolveNextCursorCreatedAt(List<Estimate> content) {
		return content.isEmpty() ? null : content.get(content.size() - 1).getCreateDate();
	}
}
