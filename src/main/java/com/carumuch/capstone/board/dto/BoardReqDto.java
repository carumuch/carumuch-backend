package com.carumuch.capstone.board.dto;

import com.carumuch.capstone.global.validation.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "BoardReqDto: 게시글 작성 요청 Dto ")
public class BoardReqDto {
    private String boardTitle;
    private String boardContent;
    private List<MultipartFile> boardImage;


    @Builder
    public BoardReqDto(String boardTitle,String boardContent,List<MultipartFile> boardImage){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardImage = boardImage;
    }

}
