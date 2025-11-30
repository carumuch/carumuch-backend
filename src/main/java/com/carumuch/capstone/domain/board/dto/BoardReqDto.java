package com.carumuch.capstone.domain.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "BoardReqDto: 게시글 작성 요청 Dto")
public class BoardReqDto {
    @Schema(description = "게시글 제목 입력", example = "boardTitle")
    private String boardTitle;
    @Schema(description = "게시글 내용 입력", example = "boardContent")
    private String boardContent;
    private List<MultipartFile> boardImage;


    @Builder
    public BoardReqDto(String boardTitle,String boardContent,List<MultipartFile> boardImage){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardImage = boardImage;
    }

}
