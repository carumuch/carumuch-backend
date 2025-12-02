package com.carumuch.capstone.estimate.domain.estimate;

import com.carumuch.capstone.estimate.presentation.dto.request.estimate.EstimateSearchReqDto;
import com.carumuch.capstone.estimate.presentation.dto.response.estimate.EstimateSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<EstimateSearchResDto> searchPage(EstimateSearchReqDto estimateSearchReqDto, Pageable pageable);
}
