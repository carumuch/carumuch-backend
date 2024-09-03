package com.carumuch.capstone.board.controller;


import com.carumuch.capstone.board.dto.BoardModifyReqDto;
import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.service.BoardService;
import com.carumuch.capstone.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController implements BoardControllerDocs{

    private final BoardService boardService;

    /**
     * Create: 게시글 작성
     */
    @Override
    public ResponseEntity<?> write(@ModelAttribute BoardReqDto boardReqDto) throws IOException {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, boardService.write(boardReqDto)));
    }

    /**
     * Select: 전체 게시글 조회
     */
    @Override
    public ResponseEntity<?> findAll(){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findAll()));
    }

    /**
     * Select: 게시글 상세 조회
     */
    @Override
    public ResponseEntity<?> findById(@PathVariable("boardId") Long id){
        boardService.updateBoardHits(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findById(id)));
    }

    /**
     * Delete: 게시글 삭제
     */
    @Override
    public ResponseEntity<?> delete(@PathVariable("boardId") Long id){
        boardService.delete(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,null));
    }

    /**
     * Update: 게시글 수정
     */
    @Override
    public ResponseEntity<?> modify(@PathVariable("boardId") Long id, @ModelAttribute BoardModifyReqDto boardModifyReqDto) throws IOException {
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.modify(id,boardModifyReqDto)));
    }
    
}
