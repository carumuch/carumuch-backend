package com.carumuch.capstone.comment.controller;

import com.carumuch.capstone.comment.dto.CommentModifyReqDto;
import com.carumuch.capstone.comment.dto.CommentReqDto;
import com.carumuch.capstone.comment.service.CommentService;
import com.carumuch.capstone.global.common.ResponseDto;
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

    @Override
    public ResponseEntity<?> writeComment(@RequestBody CommentReqDto commentReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, commentService.writeComment(commentReqDto)));
    }
    @Override
    public ResponseEntity<?> modifyComment(@PathVariable("commentId") Long id, @RequestBody CommentModifyReqDto commentModifyReqDto) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, commentService.modifyComment(id,commentModifyReqDto)));
    }

}
