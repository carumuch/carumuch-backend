package com.carumuch.capstone.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface ImageControllerDocs {

    /*이미지 업로드 */
    @Operation(summary = "이미지 업로드 요청", description = "**성공 응답 데이터:** ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "401", description = "이미지 업로드 실패"),
    })
    @PostMapping("/upload")
    ResponseEntity<?> s3Upload(@RequestParam("image") MultipartFile image);
}
