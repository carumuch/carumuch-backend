package com.carumuch.capstone.domain.comment.controller;

import com.carumuch.capstone.domain.comment.dto.CommentModifyReqDto;
import com.carumuch.capstone.domain.comment.dto.CommentReqDto;
import com.carumuch.capstone.domain.comment.service.CommentService;
import com.carumuch.capstone.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController implements CommentControllerDocs{

    private final CommentService commentService;
    /**
     * Create: 댓글 작성
     */
    @Override
    public ResponseEntity<?> writeComment(@RequestBody CommentReqDto commentReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, commentService.writeComment(commentReqDto)));
    }
    /**
     * Update: 댓글 수정
     */
    @Override
    public ResponseEntity<?> modifyComment(@PathVariable("commentId") Long id, @RequestBody CommentModifyReqDto commentModifyReqDto) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, commentService.modifyComment(id,commentModifyReqDto)));
    }

    /**
     * Delete: 게시글 삭제
     */
    @Override
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, null));
    }

}
