package com.carumuch.capstone.estimate.domain;

import com.carumuch.capstone.estimate.presentation.dto.request.EstimateSearchReqDto;
import com.carumuch.capstone.estimate.presentation.dto.response.EstimateSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<EstimateSearchResDto> searchPage(EstimateSearchReqDto estimateSearchReqDto, Pageable pageable);
}
