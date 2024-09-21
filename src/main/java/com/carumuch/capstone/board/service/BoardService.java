package com.carumuch.capstone.board.service;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.domain.BoardImage;
import com.carumuch.capstone.board.dto.BoardModifyReqDto;
import com.carumuch.capstone.board.dto.BoardReqDto;
import com.carumuch.capstone.board.repository.BoardImageRepository;
import com.carumuch.capstone.board.repository.BoardRepository;
import com.carumuch.capstone.global.auditing.UserAuditorAware;
import com.carumuch.capstone.image.service.ImageService;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final UserRepository userRepository;
    private final UserAuditorAware userAuditorAware;
    private final ImageService imageService;

    /**
     * Create: 게시글 작성
     */
    @Transactional
    public Long write(BoardReqDto boardReqDto) throws IOException {

        /*로그인한 유저정보 조회*/
        String currentUsername = userAuditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

        User user = userRepository.findByLoginId(currentUsername)
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

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
        Optional<Board> optionalBoard = boardRepository.findById(id);
        return optionalBoard.orElse(null);
    }

    /**
     * Update: 게시글 수정
     */
    @Transactional
    public Long modify(Long id, BoardModifyReqDto boardModifyReqDto) throws IOException {

        /* 로그인한 유저정보 조회 */
        String currentUsername = userAuditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

        User currentUser = userRepository.findByLoginId(currentUsername)
                .orElseThrow(()->new RuntimeException("로그인이 되어있지 않습니다"));

        /*게시글 조회*/
        Board savedBoard = boardRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("게시글이 존재하지 않습니다"));

        if(!savedBoard.getUser().equals(currentUser)){
            throw new RuntimeException("이 게시글을 수정할 권한이 없습니다");
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
                        .orElseThrow(()-> new RuntimeException("이미지가 존재하지 않습니다"));

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
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        /* 로그인한 유저정보 조회 */
        String currentUsername = userAuditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

        User currentUser = userRepository.findByLoginId(currentUsername)
                .orElseThrow(()->new RuntimeException("로그인이 되어있지 않습니다"));

        /* 게시글 작성자와 접속되어있는 유저 비교 */
        if (!board.getUser().equals(currentUser)){
            throw new RuntimeException("이 게시글을 삭제할 권한이 없습니다");
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
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));
        int boardHits = board.getBoardHits();
        board.updateBoardHits(boardHits + 1);
    }

}
