package com.carumuch.capstone.community.presentation;

import com.carumuch.capstone.community.presentation.dto.CommentModifyReqDto;
import com.carumuch.capstone.community.presentation.dto.CommentReqDto;
import com.carumuch.capstone.community.application.CommentService;
import com.carumuch.capstone.common.legacy.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    /**
     * Create: 댓글 작성
     */
	@PostMapping("/write")
    public ResponseEntity<?> writeComment(@RequestBody CommentReqDto commentReqDto) {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, commentService.writeComment(commentReqDto)));
    }
    /**
     * Update: 댓글 수정
     */
	@PutMapping("/{commentId}/modify")
    public ResponseEntity<?> modifyComment(@PathVariable("commentId") Long id, @RequestBody CommentModifyReqDto commentModifyReqDto) {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, commentService.modifyComment(id,commentModifyReqDto)));
    }

    /**
     * Delete: 게시글 삭제
     */
	@DeleteMapping("/{commentId}/delete")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long id){
        commentService.deleteComment(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK, null));
    }

}
