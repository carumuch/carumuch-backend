package com.carumuch.capstone.domain.estimate.repository.custom;

import com.carumuch.capstone.domain.estimate.dto.EstimateSearchReqDto;
import com.carumuch.capstone.domain.estimate.dto.EstimateSearchResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<EstimateSearchResDto> searchPage(EstimateSearchReqDto estimateSearchReqDto, Pageable pageable);
}
