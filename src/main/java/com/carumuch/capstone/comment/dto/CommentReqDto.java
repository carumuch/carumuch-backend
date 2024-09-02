package com.carumuch.capstone.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "CommentReqDto: 댓글 작성 요청 Dto ")
public class CommentReqDto {
    private String commentContent;
    private Long boardId;

    @Builder
    public CommentReqDto(String commentContent, Long boardId) {
        this.commentContent = commentContent;
        this.boardId = boardId;

    }
}
