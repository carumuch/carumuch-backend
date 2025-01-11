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
@Schema(name = "BoardModifyReqDto: 게시글 수정 요청 Dto ")
public class BoardModifyReqDto {
    private String boardTitle;
    private String boardContent;
    private List<MultipartFile> boardImage;
    private List<Long> deleteImage;


    @Builder
    public BoardModifyReqDto(String boardTitle,String boardContent,List<MultipartFile> boardImage,List<Long> deleteImage){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardImage = boardImage;
        this.deleteImage = deleteImage;

    }

}
