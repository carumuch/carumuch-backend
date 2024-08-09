package com.carumuch.capstone.comment.controller;

import com.carumuch.capstone.comment.dto.CommentReqDto;
import com.carumuch.capstone.comment.repository.CommentRepository;
import com.carumuch.capstone.comment.service.CommentService;
import com.carumuch.capstone.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController implements CommentControllerDocs{

    private final CommentService commentService;

    public ResponseEntity<?> write(@RequestBody CommentReqDto commentReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, commentService.write(commentReqDto)));
    }
}
