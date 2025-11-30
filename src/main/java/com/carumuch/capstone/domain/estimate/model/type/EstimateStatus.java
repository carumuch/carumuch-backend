package com.carumuch.capstone.domain.estimate.model.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EstimateStatus {
    PUBLIC("PUBLIC", "견적 공개 상태"),
    PRIVATE("PRIVATE", "견적 비공개 상태");

    private final String key;
    private final String title;
}
