package com.carumuch.capstone.board.controller;

import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.service.BoardService;
import com.carumuch.capstone.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/")
    public ResponseEntity<?> findAll(){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findAll()));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> findById(@PathVariable("boardId") Long id){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.findById(id)));
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<?> delete(@PathVariable("boardId") Long id){
        boardService.delete(id);
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,null));
    }

    @PutMapping("/{boardId}/update")
    public ResponseEntity<?> update(@PathVariable("boardId") Long id,@RequestBody BoardReqDto boardReqDto){
        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardService.update(id,boardReqDto)));
    }
    
}
