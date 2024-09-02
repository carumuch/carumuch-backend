package com.carumuch.capstone.comment.controller;

import com.carumuch.capstone.comment.dto.CommentModifyReqDto;
import com.carumuch.capstone.comment.dto.CommentReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Comment")
public interface CommentControllerDocs {
    /* Create: 게시글 작성 */
    @Operation(summary = "댓글 작성 요청", description = "**성공 응답 데이터:** 게시글의 `comment_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "댓글 작성 실패"),
    })
    @PostMapping("/write")
    ResponseEntity<?> writeComment(@RequestBody CommentReqDto commentReqDto);

    /* Update: 게시글 수정 */
    @Operation(summary = "댓글 작성 요청", description = "**성공 응답 데이터:** 게시글의 `comment_id` ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "댓글 작성 실패"),
    })
    @PutMapping("/{commentId}/modify")
    ResponseEntity<?> modifyComment(@PathVariable("commentId") Long id, @RequestBody CommentModifyReqDto commentModifyReqDto);
}
