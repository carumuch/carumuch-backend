package com.carumuch.capstone.board.controller;

import com.carumuch.capstone.board.dto.BoardReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Tag(name = "Board")
public interface BoardControllerDocs {
    /*게시글 작성*/
    @Operation(summary = "게시글 작성 요청", description = "**성공 응답 데이터:** ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "401", description = "이미지 업로드 실패"),
    })
    @PostMapping("/write")
    ResponseEntity<?> write(@RequestBody BoardReqDto boardReqDto);
}
