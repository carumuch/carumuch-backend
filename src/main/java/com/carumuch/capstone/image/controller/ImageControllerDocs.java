package com.carumuch.capstone.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Image")
public interface ImageControllerDocs {

    /*이미지 업로드 */
    @Operation(summary = "이미지 업로드 요청", description = "**성공 응답 데이터:** ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "401", description = "이미지 업로드 실패"),
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> s3Upload(@RequestParam("image") MultipartFile image) throws IOException;

    /*이미지 로드 */
    @Operation(summary = "이미지 로드 요청", description = "**성공 응답 데이터:** ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 로드 성공"),
            @ApiResponse(responseCode = "401", description = "이미지 로드 실패"),
    })
    @GetMapping("/load")
    ResponseEntity<?> s3Load(@RequestParam("imageKey") String imageKey);

    /*이미지 로드 */
    @Operation(summary = "이미지 삭제 요청", description = "**성공 응답 데이터:** ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "이미지 삭제 실패"),
    })
    @DeleteMapping("/delete")
    ResponseEntity<?> s3Delete(@RequestParam("imagePath") String imagePath);
}
