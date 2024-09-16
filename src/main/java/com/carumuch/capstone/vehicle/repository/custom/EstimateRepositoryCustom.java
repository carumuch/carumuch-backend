package com.carumuch.capstone.vehicle.repository.custom;

import com.carumuch.capstone.vehicle.dto.estimate.EstimateSearchReqDto;
import com.carumuch.capstone.vehicle.dto.estimate.EstimateSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<EstimateSearchResDto> searchPage(EstimateSearchReqDto estimateSearchReqDto, Pageable pageable);
}
