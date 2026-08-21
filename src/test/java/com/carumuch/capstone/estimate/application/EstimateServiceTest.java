package com.carumuch.capstone.estimate.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.carumuch.capstone.common.exception.CustomException;
import com.carumuch.capstone.common.exception.ForbiddenException;
import com.carumuch.capstone.common.exception.NotFoundException;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollQuery;
import com.carumuch.capstone.estimate.application.dto.EstimateScrollSlice;
import com.carumuch.capstone.estimate.application.dto.EstimateSearchCondition;
import com.carumuch.capstone.estimate.domain.Estimate;
import com.carumuch.capstone.estimate.domain.EstimateRepository;
import com.carumuch.capstone.estimate.domain.EstimateStatus;
import com.carumuch.capstone.estimate.presentation.dto.request.EstimateScrollRequest;
import com.carumuch.capstone.estimate.presentation.dto.request.SearchEstimateRequest;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateDetailResponse;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateScrollResponse;
import com.carumuch.capstone.support.fixture.EstimateFixture;

@ExtendWith(MockitoExtension.class)
class EstimateServiceTest {

	@InjectMocks
	EstimateService estimateService;

	@Mock
	EstimateRepository estimateRepository;

	@Nested
	@DisplayName("견적서 상세 조회 기능")
	class FindEstimateDetail {
		@Test
		void 견적서_PK로_견적서를_조회한다() {
		    //given
			Long estimateId = 300L;
			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			Mockito.when(estimateRepository.findDetailById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

		    //when
		    estimateService.findEstimateDetail(estimateId);

		    //then
			Mockito.verify(estimateRepository, Mockito.times(1))
				.findDetailById(estimateId);
		}

		@Test
		void 견적서가_존재하지_않는다면_예외를_반환한다() {
		    //given
			Long estimateId = 300L;
			Mockito.when(estimateRepository.findDetailById(estimateId))
				.thenReturn(Optional.empty());

		    //when & then
			assertThatThrownBy(() -> estimateService.findEstimateDetail(estimateId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 견적서_상세_정보를_응답한다() {
		    //given
			Long estimateId = 300L;

			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			ReflectionTestUtils.setField(estimateFixture, "id", estimateId);

			Mockito.when(estimateRepository.findDetailById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

			EstimateDetailResponse estimateDetailResponse = new EstimateDetailResponse(estimateFixture);

		    //when
			EstimateDetailResponse result = estimateService.findEstimateDetail(estimateId);

			//then
			assertAll(
				() -> assertThat(result.estimateId()).isEqualTo(estimateDetailResponse.estimateId()),
				() -> assertThat(result.repairCost()).isEqualTo(estimateDetailResponse.repairCost()),
				() -> assertThat(result.repairParts().get(0)).isEqualTo(estimateDetailResponse.repairParts().get(0)),
				() -> assertThat(result.repairParts().get(1)).isEqualTo(estimateDetailResponse.repairParts().get(1)),
				() -> assertThat(result.repairParts().get(2)).isEqualTo(estimateDetailResponse.repairParts().get(2)),
				() -> assertThat(result.estimateStatus()).isEqualTo(estimateDetailResponse.estimateStatus()),
				() -> assertThat(result.imagePath()).isEqualTo(estimateDetailResponse.imagePath())
			);

			assertAll(
				() -> assertThat(result.vehicleInfo().brand()).isEqualTo(estimateDetailResponse.vehicleInfo().brand()),
				() -> assertThat(result.vehicleInfo().licenseNumber()).isEqualTo(estimateDetailResponse.vehicleInfo().licenseNumber()),
				() -> assertThat(result.vehicleInfo().modelName()).isEqualTo(estimateDetailResponse.vehicleInfo().modelName()),
				() -> assertThat(result.vehicleInfo().modelYear()).isEqualTo(estimateDetailResponse.vehicleInfo().modelYear()),
				() -> assertThat(result.vehicleInfo().ownerName()).isEqualTo(estimateDetailResponse.vehicleInfo().ownerName()),
				() -> assertThat(result.vehicleInfo().ownershipType()).isEqualTo(estimateDetailResponse.vehicleInfo().ownershipType())
			);

			assertAll(
				() -> assertThat(result.damageReportInfo().description()).isEqualTo(estimateDetailResponse.damageReportInfo().description()),
				() -> assertThat(result.damageReportInfo().preferredRepairSido()).isEqualTo(estimateDetailResponse.damageReportInfo().preferredRepairSido()),
				() -> assertThat(result.damageReportInfo().preferredRepairSigungu()).isEqualTo(estimateDetailResponse.damageReportInfo().preferredRepairSigungu()),
				() -> assertThat(result.damageReportInfo().isPickupRequired()).isEqualTo(estimateDetailResponse.damageReportInfo().isPickupRequired())
			);
		}
	}

	@Nested
	@DisplayName("사고 레포트를 통한 견적서 상세 조회 기능")
	class FindEstimateDetailByDamageReportId {
		@Test
		void 사고레포트_PK로_견적서를_조회한다() {
			//given
			Long damageReportId = 200L;
			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			Mockito.when(estimateRepository.findDetailByDamageReportId(damageReportId))
				.thenReturn(Optional.of(estimateFixture));

			//when
			estimateService.findEstimateDetailByDamageReportId(damageReportId);

			//then
			Mockito.verify(estimateRepository, Mockito.times(1))
				.findDetailByDamageReportId(damageReportId);
		}

		@Test
		void 견적서가_존재하지_않는다면_예외를_반환한다() {
			//given
			Long damageReportId = 200L;
			Mockito.when(estimateRepository.findDetailByDamageReportId(damageReportId))
				.thenReturn(Optional.empty());

			//when & then
			assertThatThrownBy(() -> estimateService.findEstimateDetailByDamageReportId(damageReportId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 견적서_상세_정보를_응답한다() {
			//given
			Long damageReportId = 200L;

			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();

			Long estimateId = 300L;
			ReflectionTestUtils.setField(estimateFixture, "id", estimateId);

			Mockito.when(estimateRepository.findDetailByDamageReportId(damageReportId))
				.thenReturn(Optional.of(estimateFixture));

			EstimateDetailResponse estimateDetailResponse = new EstimateDetailResponse(estimateFixture);

			//when
			EstimateDetailResponse result = estimateService.findEstimateDetailByDamageReportId(damageReportId);

			//then
			assertAll(
				() -> assertThat(result.estimateId()).isEqualTo(estimateDetailResponse.estimateId()),
				() -> assertThat(result.repairCost()).isEqualTo(estimateDetailResponse.repairCost()),
				() -> assertThat(result.repairParts().get(0)).isEqualTo(estimateDetailResponse.repairParts().get(0)),
				() -> assertThat(result.repairParts().get(1)).isEqualTo(estimateDetailResponse.repairParts().get(1)),
				() -> assertThat(result.repairParts().get(2)).isEqualTo(estimateDetailResponse.repairParts().get(2)),
				() -> assertThat(result.estimateStatus()).isEqualTo(estimateDetailResponse.estimateStatus()),
				() -> assertThat(result.imagePath()).isEqualTo(estimateDetailResponse.imagePath())
			);

			assertAll(
				() -> assertThat(result.vehicleInfo().brand()).isEqualTo(estimateDetailResponse.vehicleInfo().brand()),
				() -> assertThat(result.vehicleInfo().licenseNumber()).isEqualTo(estimateDetailResponse.vehicleInfo().licenseNumber()),
				() -> assertThat(result.vehicleInfo().modelName()).isEqualTo(estimateDetailResponse.vehicleInfo().modelName()),
				() -> assertThat(result.vehicleInfo().modelYear()).isEqualTo(estimateDetailResponse.vehicleInfo().modelYear()),
				() -> assertThat(result.vehicleInfo().ownerName()).isEqualTo(estimateDetailResponse.vehicleInfo().ownerName()),
				() -> assertThat(result.vehicleInfo().ownershipType()).isEqualTo(estimateDetailResponse.vehicleInfo().ownershipType())
			);

			assertAll(
				() -> assertThat(result.damageReportInfo().description()).isEqualTo(estimateDetailResponse.damageReportInfo().description()),
				() -> assertThat(result.damageReportInfo().preferredRepairSido()).isEqualTo(estimateDetailResponse.damageReportInfo().preferredRepairSido()),
				() -> assertThat(result.damageReportInfo().preferredRepairSigungu()).isEqualTo(estimateDetailResponse.damageReportInfo().preferredRepairSigungu()),
				() -> assertThat(result.damageReportInfo().isPickupRequired()).isEqualTo(estimateDetailResponse.damageReportInfo().isPickupRequired())
			);
		}
	}

	@Nested
	@DisplayName("견적서 상태 업데이트 기능")
	class UpdateEstimateStatus {
		@Test
		void 견적서_PK로_견적서를_조회한다() {
		    //given
			Long estimateId = 300L;
			String estimateStatus = EstimateStatus.PRIVATE.name();

			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

			//when
			estimateService.changeStatus(estimateId, estimateStatus, estimateFixture.getUserId());

		    //then
			Mockito.verify(estimateRepository, Mockito.times(1))
				.findById(estimateId);
		}

		@Test
		void 견적서를_찾지_못하면_예외를_반환한다() {
		    //given
			Long estimateId = 300L;
			Long userId = 10L;
			String estimateStatus = EstimateStatus.PRIVATE.name();
			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.empty());

		    //when & then
			assertThatThrownBy(() -> estimateService.changeStatus(estimateId, estimateStatus, userId))
				.isInstanceOf(NotFoundException.class);
		}

		@Test
		void 견적서_상태를_업데이트_한다() {
		    //given
			Long estimateId = 300L;
			String estimateStatus = EstimateStatus.PRIVATE.name();

			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();

			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

		    //when
			estimateService.changeStatus(estimateId, estimateStatus, estimateFixture.getUserId());

		    //then
			Assertions.assertThat(estimateFixture.getEstimateStatus()).isEqualTo(EstimateStatus.valueOf(estimateStatus));
		}

		@Test
		void 이미_매칭된_견적서의_상태를_변경하면_예외를_반환한다() {
		    //given
			Long estimateId = 300L;
			String estimateStatus = EstimateStatus.PRIVATE.name();

			Estimate alreadyMatchedEstimateFixture = EstimateFixture.ESTIMATE_FIXTURE_2.create();
			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.of(alreadyMatchedEstimateFixture));

		    //when & then
			assertThatThrownBy(() -> estimateService.changeStatus(estimateId, estimateStatus, alreadyMatchedEstimateFixture.getUserId()))
				.isInstanceOf(CustomException.class);
		}

		@Test
		void 자신의_견적서가_아니라면_수정할_수_없다() {
			//given
			Long estimateId = 300L;
			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			String estimateStatus = EstimateStatus.PRIVATE.name();
			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

			Long anotherUserId = 20L;

			//when & then
			assertThatThrownBy(() -> estimateService.changeStatus(estimateId, estimateStatus, anotherUserId))
				.isInstanceOf(ForbiddenException.class);
		}

		@Test
		void 견적서를_낙찰된_상태로_변경할_수_없다() {
		    //given
			Long estimateId = 300L;
			Estimate estimateFixture = EstimateFixture.ESTIMATE_FIXTURE_1.create();
			String estimateStatus = EstimateStatus.CLOSED.name();

			Mockito.when(estimateRepository.findById(estimateId))
				.thenReturn(Optional.of(estimateFixture));

		    //when & then
			assertThatThrownBy(() -> estimateService.changeStatus(estimateId, estimateStatus, estimateFixture.getUserId()))
				.isInstanceOf(CustomException.class);
		}
	}

	@Nested
	@DisplayName("견적서 조건 검색 기능")
	class SearchEstimates {
		@Test
		void 견적서를_조회한다() {
		    //given
			SearchEstimateRequest searchEstimateRequest = new SearchEstimateRequest(
				null, null, null, null, null, null, null, null
			);
			EstimateSearchCondition estimateSearchCondition = EstimateSearchCondition.from(searchEstimateRequest);
			EstimateScrollRequest scrollRequest = new EstimateScrollRequest(null, null, null);
			EstimateScrollQuery scrollQuery = EstimateScrollQuery.from(scrollRequest);

			List<Estimate> estimateList = List.of(
				EstimateFixture.ESTIMATE_FIXTURE_1.create(),
				EstimateFixture.ESTIMATE_FIXTURE_2.create()
			);
			EstimateScrollSlice<Estimate> estimates = new EstimateScrollSlice<>(estimateList, false,
				estimateList.get(estimateList.size() - 1).getId(),
				estimateList.get(estimateList.size() - 1).getCreateDate());

			Mockito.when(estimateRepository.searchEstimates(estimateSearchCondition, scrollQuery))
				.thenReturn(estimates);

			//when
			EstimateScrollResponse result = estimateService.searchEstimates(searchEstimateRequest, scrollRequest);

		    //then
			Mockito.verify(estimateRepository, Mockito.times(1))
				.searchEstimates(estimateSearchCondition, scrollQuery);
			assertThat(result.content()).hasSize(2);
			assertThat(result.hasNext()).isFalse();
		}
	}
}
