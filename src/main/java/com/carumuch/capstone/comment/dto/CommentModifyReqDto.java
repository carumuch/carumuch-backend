package com.carumuch.capstone.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "CommentReqDto: 댓글 수정 요청 Dto ")
public class CommentModifyReqDto {
    private String commentContent;

    @Builder
    public CommentModifyReqDto(String commentContent) {
        this.commentContent = commentContent;
    }
}
