package com.carumuch.capstone.domain.board.service;

import com.carumuch.capstone.domain.board.model.Board;
import com.carumuch.capstone.domain.board.model.BoardImage;
import com.carumuch.capstone.domain.board.dto.BoardModifyReqDto;
import com.carumuch.capstone.domain.board.dto.BoardReqDto;
import com.carumuch.capstone.domain.board.repository.BoardImageRepository;
import com.carumuch.capstone.domain.board.repository.BoardRepository;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
import com.carumuch.capstone.domain.image.service.ImageService;
import com.carumuch.capstone.domain.user.model.User;
import com.carumuch.capstone.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    /**
     * Create: 게시글 작성
     */
    @Transactional
    public Long write(BoardReqDto boardReqDto){

        /*로그인한 유저정보 조회*/
        User user =  userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        Board board = boardRepository.save(Board.builder()
                        .user(user)
                .boardTitle(boardReqDto.getBoardTitle())
                .boardContent(boardReqDto.getBoardContent())
                .boardHits(0)
                .build());

        /*이미지 첨부 여부 확인 후 이미지  저장*/
        if(boardReqDto.getBoardImage() != null){
            /*이미지 저장*/
            for (MultipartFile image : boardReqDto.getBoardImage()){
                String savedImagePath = imageService.uploadImage(image);
                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .originalImageName(image.getOriginalFilename())
                        .savedImageName(savedImagePath)
                        .build();
                boardImageRepository.save(boardImage);
            }
        }



        return board.getId();
    }

    /**
     * Select: 전체 게시글 조회
     */
    public Page<Board> findAll(Pageable pageable){
        int page = pageable.getPageNumber() - 1;
        /*한 페이지 당 글 개수*/
        int pageLimit = 20;
        return boardRepository.findAll(PageRequest.of(page,pageLimit, Sort.by(Sort.Direction.DESC,"id")));
    }

    /**
     * Select: 게시글 상세 조회
     */
    public Board findById(Long id){
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        return board;
    }

    /**
     * Update: 게시글 수정
     */
    @Transactional
    public Long modify(Long id, BoardModifyReqDto boardModifyReqDto) {

        /* 로그인한 유저정보 조회 */
        User user =  userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        /*게시글 조회*/
        Board savedBoard = boardRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!savedBoard.getUser().equals(user)){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        /*이미지 첨부 여부 확인 후 이미지  저장*/
        if(boardModifyReqDto.getBoardImage() != null){
            /*이미지 저장*/
            for (MultipartFile image : boardModifyReqDto.getBoardImage()){
                String savedImagePath = imageService.uploadImage(image);
                BoardImage boardImage = BoardImage.builder()
                        .board(savedBoard)
                        .originalImageName(image.getOriginalFilename())
                        .savedImageName(savedImagePath)
                        .build();
                boardImageRepository.save(boardImage);
            }
        }
        /*게시글 내 첨부된 이미지 삭제*/
        if (boardModifyReqDto.getDeleteImage() != null){
            /*이미지 삭제*/
            for(Long image_id : boardModifyReqDto.getDeleteImage() ){
                BoardImage deleteBoardImage = boardImageRepository.findById(image_id)
                        .orElseThrow(()-> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

                String savedImageName = deleteBoardImage.getSavedImageName();
                imageService.deleteImage(savedImageName);
                boardImageRepository.delete(deleteBoardImage);
            }
        }


        savedBoard.updateBoard(boardModifyReqDto.getBoardTitle(), boardModifyReqDto.getBoardContent());

        return id;
    }

    /**
     * Delete: 게시글 삭제
     */
    public void delete(Long id){
        /* 게시글 조회 */
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        /* 로그인한 유저정보 조회 */
        User user =  userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        /* 게시글 작성자와 접속되어있는 유저 비교 */
        if (!board.getUser().equals(user)){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        if(board.getBoardImages() != null){
            /*이미지 삭제*/

            for (BoardImage boardImage : board.getBoardImages()){
                String savedImageName = boardImage.getSavedImageName();
                imageService.deleteImage(savedImageName);
            }

        }
        boardRepository.delete(board);

    }

    /**
     * Update: 게시글 조회시 조회수 증가
     */
    @Transactional
    public  void updateBoardHits(Long id){
        /* 게시글 조회 */
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        int boardHits = board.getBoardHits();
        board.updateBoardHits(boardHits + 1);
    }

}
