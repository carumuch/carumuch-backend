package com.carumuch.capstone.board.controller;


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


    @Override
    public ResponseEntity<?> write(@ModelAttribute BoardReqDto boardReqDto) throws IOException {
        System.out.println(boardReqDto.getBoardTitle());
        System.out.println(boardReqDto.getBoardContent());
        System.out.println(boardReqDto.getBoardImage());
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, boardService.write(boardReqDto)));
    }

    @Override
    public ResponseEntity<?> findAll(){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findAll()));
    }

    @Override
    public ResponseEntity<?> findById(@PathVariable("boardId") Long id){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findById(id)));
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable("boardId") Long id){
        boardService.delete(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,null));
    }

    @Override
    public ResponseEntity<?> update(@PathVariable("boardId") Long id,@RequestBody BoardReqDto boardReqDto){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.update(id,boardReqDto)));
    }
    
}
