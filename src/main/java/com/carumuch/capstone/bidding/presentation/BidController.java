package com.carumuch.capstone.bidding.presentation;

import com.carumuch.capstone.bidding.application.BidService;
import com.carumuch.capstone.bidding.presentation.dto.response.BidInfoResponse;
import com.carumuch.capstone.bidding.presentation.dto.response.BidListResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;
import com.carumuch.capstone.identity.domain.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping("history/{estimateId}")
    public ResponseEntity<ApiResponse<PagingResponse<BidListResponse>>> getBids(
        @PathVariable Long estimateId,
        @ModelAttribute PagingRequest pagingRequest
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(bidService.getBids(estimateId, pagingRequest)));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ApiResponse<BidInfoResponse>> bidDetail(@PathVariable Long BidId) {
        return ResponseEntity.ok().body(ApiResponse.of(bidService.detailBid(BidId)));
    }

    @PatchMapping("/{bidId}")
    public ResponseEntity<ApiResponse<Void>> bidStatusUpdate(@PathVariable Long bidId, User user) {
        bidService.acceptBid(bidId, user.getId());
        return ResponseEntity.status(CREATED).body(ApiResponse.of());
    }
}
