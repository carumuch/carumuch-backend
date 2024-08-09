package com.carumuch.capstone.comment.service;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.repository.BoardRepository;
import com.carumuch.capstone.comment.domain.Comment;
import com.carumuch.capstone.comment.dto.CommentReqDto;
import com.carumuch.capstone.comment.repository.CommentRepository;
import com.carumuch.capstone.global.auditing.UserAuditorAware;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserAuditorAware userAuditorAware;
    private final BoardRepository boardRepository;

    public Long write(CommentReqDto commentReqDto){
        /*로그인한 유저정보 조회*/
        String currentUsername = userAuditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

        User user = userRepository.findByLoginId(currentUsername)
                .orElseThrow(() -> new RuntimeException("로그인이 되어있지 않습니다"));

        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .board(board)
                .commentContent(commentReqDto.getCommentContent())
                .build());

        return comment.getId();

    }
}