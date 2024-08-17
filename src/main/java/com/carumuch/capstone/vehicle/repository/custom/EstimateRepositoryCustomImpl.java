package com.carumuch.capstone.vehicle.repository.custom;


import com.carumuch.capstone.vehicle.domain.Estimate;
import com.carumuch.capstone.vehicle.domain.type.EstimateStatus;
import com.carumuch.capstone.vehicle.dto.EstimateSearchReqDto;
import com.carumuch.capstone.vehicle.dto.EstimateSearchResDto;
import com.carumuch.capstone.vehicle.dto.QEstimateSearchResDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.carumuch.capstone.vehicle.domain.QEstimate.*;
import static com.carumuch.capstone.vehicle.domain.QVehicle.vehicle;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class EstimateRepositoryCustomImpl implements EstimateRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EstimateRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<EstimateSearchResDto> searchPage(EstimateSearchReqDto estimateSearchReqDto, Pageable pageable) {

        /* 메인 쿼리 */
        List<EstimateSearchResDto> content = queryFactory
                .select(new QEstimateSearchResDto(
                        estimate.id.as("id"),
                        estimate.damageArea.as("damageArea"),
                        estimate.preferredRepairSido.as("preferredRepairSido"),
                        estimate.preferredRepairSigungu.as("preferredRepairSigungu"),
                        estimate.aiEstimatedRepairCost.as("aiEstimatedRepairCost"),
                        estimate.isAIEstimate.as("isAIEstimate"),
                        estimate.createDate.as("createDate"),
                        estimate.applicantCount.as("applicantCount"),
                        vehicle.type.as("type"),
                        vehicle.brand.as("brand"),
                        vehicle.modelYear.as("modelYear"),
                        vehicle.modelName.as("modelName"),
                        estimate.isPickupRequired.as("isPickupRequired")
                ))
                .from(estimate)
                .leftJoin(estimate.vehicle, vehicle)
                .where(
                        estimate.estimateStatus.eq(EstimateStatus.PUBLIC),
                        damageAreaEq(estimateSearchReqDto.getDamageArea()),
                        preferredRepairSidoEq(estimateSearchReqDto.getPreferredRepairSido()),
                        preferredRepairSigunguEq(estimateSearchReqDto.getPreferredRepairSigungu()),
                        brandEq(estimateSearchReqDto.getBrand()),
                        modelYearEq(estimateSearchReqDto.getModelYear()),
                        modelNameEq(estimateSearchReqDto.getModelName()),
                        isPickupRequiredEq(estimateSearchReqDto.getIsPickupRequired()),
                        isAIEstimateEq(estimateSearchReqDto.getIsAIEstimate()),
                        aiEstimatedRepairCostEq(estimateSearchReqDto.getAiEstimatedRepairCost()),
                        vehicle.isNotNull()
                )
                .orderBy(getOrderSpecifier(estimateSearchReqDto.getOrder()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /* 카운트 쿼리 */
        JPAQuery<Estimate> countQuery = queryFactory
                .select(estimate)
                .from(estimate)
                .leftJoin(estimate.vehicle, vehicle)
                .where(
                        estimate.estimateStatus.eq(EstimateStatus.PUBLIC),
                        damageAreaEq(estimateSearchReqDto.getDamageArea()),
                        preferredRepairSidoEq(estimateSearchReqDto.getPreferredRepairSido()),
                        preferredRepairSigunguEq(estimateSearchReqDto.getPreferredRepairSigungu()),
                        brandEq(estimateSearchReqDto.getBrand()),
                        modelYearEq(estimateSearchReqDto.getModelYear()),
                        modelNameEq(estimateSearchReqDto.getModelName()),
                        isPickupRequiredEq(estimateSearchReqDto.getIsPickupRequired()),
                        isAIEstimateEq(estimateSearchReqDto.getIsAIEstimate()),
                        aiEstimatedRepairCostEq(estimateSearchReqDto.getAiEstimatedRepairCost()),
                        vehicle.isNotNull()
                );
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    /* 정렬 기준 */
    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        if (sort != null) {
            return switch (sort) {
                case "POPULAR" -> estimate.applicantCount.desc();
                default -> estimate.createDate.desc(); // 기본 최신 순으로 진행
            };
        } else {
            return estimate.createDate.desc(); // 정렬이 없다면 최신 순으로 진행
        }
    }

    /* 차량 파손 부위 */
    private BooleanExpression damageAreaEq(String damageArea) {
        return hasText(damageArea) ? estimate.damageArea.eq(damageArea) : null;
    }

    /* 희망 수리 시/도 */
    private BooleanExpression preferredRepairSidoEq(String preferredRepairSido) {
        return hasText(preferredRepairSido) ? estimate.preferredRepairSido.eq(preferredRepairSido) : null;
    }

    /* 희망 수리 시/군/구 */
    private BooleanExpression preferredRepairSigunguEq(String preferredRepairSigungu) {
        return hasText(preferredRepairSigungu) ? estimate.preferredRepairSigungu.eq(preferredRepairSigungu) : null;
    }

    /* 차량 브랜드 */
    private BooleanExpression brandEq(String brand) {
        return hasText(brand) ? vehicle.brand.eq(brand) : null;
    }

    /* 차량 연형 */
    private BooleanExpression modelYearEq(Integer modelYear) {
        return modelYear != null ? vehicle.modelYear.eq(modelYear) : null;
    }

    /* 차량 모델 */
    private BooleanExpression modelNameEq(String modelName) {
        return hasText(modelName) ? vehicle.modelName.eq(modelName) : null;
    }

    /* 픽업 희망 여부 */
    private BooleanExpression isPickupRequiredEq(Boolean isPickupRequired) {
        return isPickupRequired != null ? estimate.isPickupRequired.eq(isPickupRequired) : null;
    }

    /* AI 기반 견적서 여부 */
    private BooleanExpression isAIEstimateEq(Boolean isAIEstimate) {
        return isAIEstimate != null ? estimate.isAIEstimate.eq(isAIEstimate) : null;
    }

    /* AI 기반 견적 가격 */
    private BooleanExpression aiEstimatedRepairCostEq(Integer aiEstimatedRepairCost) {
        return aiEstimatedRepairCost != null ? estimate.aiEstimatedRepairCost.eq(aiEstimatedRepairCost) : null;
    }
}
