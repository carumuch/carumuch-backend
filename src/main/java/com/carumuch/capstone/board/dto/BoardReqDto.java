package com.carumuch.capstone.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "BoardReqDto: 게시글 작성 요청 Dto ")
public class BoardReqDto {
    @Schema(description = "게시글 제목이 작성되어야 합니다")
    private String boardTitle;

    @Schema(description = "게시글 본문이 작성되어야 합니다")
    private String boardContent;

    @Builder
    public BoardReqDto(String boardTitle,String boardContent){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
