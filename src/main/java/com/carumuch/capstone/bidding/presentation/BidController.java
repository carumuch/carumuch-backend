package com.carumuch.capstone.bidding.presentation;

import com.carumuch.capstone.bidding.application.BidService;
import com.carumuch.capstone.bidding.presentation.dto.response.BidInfoResponse;
import com.carumuch.capstone.bidding.presentation.dto.response.BidListResponse;
import com.carumuch.capstone.common.presentation.dto.ApiResponse;
import com.carumuch.capstone.common.presentation.dto.PagingRequest;
import com.carumuch.capstone.common.presentation.dto.PagingResponse;

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
    public ResponseEntity<ApiResponse<PagingResponse<BidListResponse>>> bidPage(
        @PathVariable Long estimateId,
        @ModelAttribute PagingRequest pagingRequest
    ) {
        return ResponseEntity.ok().body(ApiResponse.of(bidService.findPageByEstimateId(estimateId, pagingRequest)));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ApiResponse<BidInfoResponse>> bidDetail(@PathVariable Long BidId) {
        return ResponseEntity.ok().body(ApiResponse.of(bidService.detailBid(BidId)));
    }

    @PatchMapping("/{bidId}")
    public ResponseEntity<ApiResponse<Void>> bidStatusUpdate(@PathVariable Long bidId) {
        bidService.acceptBidding(bidId);
        return ResponseEntity.status(CREATED).body(ApiResponse.of());
    }
}
