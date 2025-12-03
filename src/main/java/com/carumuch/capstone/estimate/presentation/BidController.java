package com.carumuch.capstone.estimate.presentation;

import com.carumuch.capstone.estimate.presentation.dto.request.bidding.BidStatusUpdateReqDto;
import com.carumuch.capstone.estimate.application.BiddingService;
import com.carumuch.capstone.common.legacy.dto.ResponseDto;
import com.carumuch.capstone.common.legacy.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {
    private final BiddingService biddingService;

    @GetMapping("history/{estimateId}")
    public ResponseEntity<?> bidPage(@RequestParam(defaultValue = "1") int page,
                                     @PathVariable("estimateId") Long id) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, biddingService.findPageByEstimateId(page, id)));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<?> bidDetail(@PathVariable("bidId") Long id) {
        return ResponseEntity.status(OK).body(ResponseDto.success(OK, biddingService.detailBid(id)));
    }

    @PatchMapping("/{bidId}")
    public ResponseEntity<?> bidStatusUpdate(@PathVariable("bidId") Long id,
                                             @Validated(ValidationSequence.class) @RequestBody BidStatusUpdateReqDto bidStatusUpdateReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, biddingService.updateBidStatus(id, bidStatusUpdateReqDto.getStatus())));
    }
}
