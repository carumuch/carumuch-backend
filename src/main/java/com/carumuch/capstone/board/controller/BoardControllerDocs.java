package com.carumuch.capstone.board.controller;

import com.carumuch.capstone.board.dto.BoardReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Tag(name = "Board")
public interface BoardControllerDocs {
    /* Create: 게시글 작성 */
    @Operation(summary = "게시글 작성 요청", description = "**성공 응답 데이터:** 게시글의 `board_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "게시글 작성 실패"),
    })
    @PostMapping("/write")
    ResponseEntity<?> write(@RequestPart BoardReqDto boardReqDto) throws IOException;

    /* Read: 게시글 조회 */
    @Operation(summary = "게시글 전체 조회 요청", description = "**성공 응답 데이터:** true")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 전체 조회 성공"),
            @ApiResponse(responseCode = "401", description = "게시글 전체 조회 실패"),
    })
    @GetMapping("/")
    ResponseEntity<?> findAll();

    @Operation(summary = "게시글 상세 조회 요청", description = "**성공 응답 데이터:** true")
    @Parameter(name = "boardId", description = "조회할 게시글의 번호", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공"),
            @ApiResponse(responseCode = "401", description = "게시글 상세 조회 실패"),
    })
    @GetMapping("/{boardId}")
    ResponseEntity<?> findById(@PathVariable("boardId") Long id);

    /* Update: 게시글 수정 */
    @Operation(summary = "게시글 수정 요청", description = "**성공 응답 데이터:** 게시글의 `board_id`")
    @Parameter(name = "boardId", description = "수정할 게시글의 번호", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "401", description = "게시글 수정 실패"),
    })
    @PutMapping("/{boardId}/update")
    ResponseEntity<?> update(@PathVariable("boardId") Long id,@RequestBody BoardReqDto boardReqDto);

    /* Delete: 게시글 삭제 */
    @Operation(summary = "게시글 삭제 요청", description = "**성공 응답 데이터:** ")
    @Parameter(name = "boardId", description = "삭제할 게시글의 번호", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "게시글 삭제 실패"),
    })
    @DeleteMapping("/{boardId}/delete")
    ResponseEntity<?> delete(@PathVariable("boardId") Long id);
}
