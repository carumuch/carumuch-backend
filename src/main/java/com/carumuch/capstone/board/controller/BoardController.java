package com.carumuch.capstone.board.controller;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.service.BoardService;
import com.carumuch.capstone.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController implements BoardControllerDocs{

    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody BoardReqDto boardReqDto){
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, boardService.write(boardReqDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findById(id)));
    }
    
}
