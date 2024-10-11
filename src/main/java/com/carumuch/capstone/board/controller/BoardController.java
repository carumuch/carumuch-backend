package com.carumuch.capstone.board.controller;


import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.dto.BoardModifyReqDto;
import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.service.BoardService;
import com.carumuch.capstone.global.common.ResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController implements BoardControllerDocs{

    private final int COOKIE_EXPIRATION = 60 * 60 * 24;
    private final BoardService boardService;

    /**
     * Create: 게시글 작성
     */
    @Override
    public ResponseEntity<?> write(@RequestBody BoardReqDto boardReqDto) throws IOException {
        return ResponseEntity.status(CREATED)
                .body(ResponseDto.success(CREATED, boardService.write(boardReqDto)));
    }

    /**
     * Select: 전체 게시글 조회
     */
    @Override
    //기본 페이지 1
    public ResponseEntity<?> findAll(@ParameterObject @PageableDefault(page = 1) Pageable pageable){
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
    public ResponseEntity<?> findById(@PathVariable("boardId") Long id, Pageable pageable, HttpServletRequest request, HttpServletResponse response){

        /*게시글 조회시 넘어온 페이지 넘버*/
        int pageNumber = pageable.getPageNumber();

        Map<String, Object> boardDetailResponse = new HashMap<>();
        Board board = boardService.findById(id);

        boardDetailResponse.put("board",board);
        boardDetailResponse.put("pageNumber",pageNumber);

        /*쿠키값 추출*/
        Cookie oldBoardToken = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("board-token")) {
                oldBoardToken = cookie;
            }
        }
        /*조회수 업데이트*/
        if (oldBoardToken != null) {
            if (!oldBoardToken.getValue().contains("["+ id +"]")) {
                boardService.updateBoardHits(id);
                oldBoardToken.setValue(oldBoardToken.getValue() + "[" + id + "]");
            }
        } else {
            boardService.updateBoardHits(id);
            oldBoardToken = new Cookie("board-token", "[" + id + "]");

        }
        oldBoardToken.setPath("/");
        oldBoardToken.setMaxAge(COOKIE_EXPIRATION);
        response.addCookie(oldBoardToken);



        return ResponseEntity.status(OK)
                .body(ResponseDto.success(OK,boardDetailResponse));
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
