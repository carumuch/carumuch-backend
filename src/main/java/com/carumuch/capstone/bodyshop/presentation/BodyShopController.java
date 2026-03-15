package com.carumuch.capstone.bodyshop.presentation;

import com.carumuch.capstone.bodyshop.presentation.dto.request.RegisterBodyShopRequest;
import com.carumuch.capstone.bodyshop.presentation.dto.request.UpdateBodyShopRequest;
import com.carumuch.capstone.bodyshop.application.BodyShopService;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopInfoResponse;
import com.carumuch.capstone.bodyshop.presentation.dto.response.BodyShopListResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.identity.domain.user.User;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/body-shops")
@RequiredArgsConstructor
public class BodyShopController {
    private final BodyShopService bodyShopService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> register(@RequestBody RegisterBodyShopRequest requestDto, User user) {
        return ResponseEntity.status(CREATED).body(ApiResponse.of(bodyShopService.register(requestDto, user.getId())));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagingResponse<BodyShopListResponse>>> searchKeyword(@ModelAttribute PagingRequest pagingRequest, @RequestParam String keyword) {
        return ResponseEntity.ok().body(ApiResponse.of(bodyShopService.searchKeyword(pagingRequest, keyword)));
    }

    @PutMapping("/{bodyShopId}")
    public ResponseEntity<ApiResponse<Void>> update(@RequestBody UpdateBodyShopRequest requestDto, @PathVariable Long bodyShopId, User user) {
		bodyShopService.update(bodyShopId, requestDto, user.getId());
        return ResponseEntity.ok().body(ApiResponse.of());
    }

    @GetMapping("/{bodyShopId}")
    public ResponseEntity<ApiResponse<BodyShopInfoResponse>> detail(@PathVariable Long bodyShopId) {
        return ResponseEntity.ok().body(ApiResponse.of(bodyShopService.info(bodyShopId)));
    }
}
