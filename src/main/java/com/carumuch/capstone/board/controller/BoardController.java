package com.carumuch.capstone.board.controller;


import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.dto.BoardModifyReqDto;
import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.service.BoardService;
import com.carumuch.capstone.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

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
    //기본 페이지 1
    public ResponseEntity<?> findAll(@PageableDefault(page = 1) Pageable pageable){
        Page<Board> boards = boardService.findAll(pageable);
        int blockLimit = 5;
        int startPage =  (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), boards.getTotalPages());

        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boards));
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
