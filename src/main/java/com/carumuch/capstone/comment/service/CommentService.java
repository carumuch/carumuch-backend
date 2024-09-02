package com.carumuch.capstone.comment.service;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.repository.BoardRepository;
import com.carumuch.capstone.comment.domain.Comment;
import com.carumuch.capstone.comment.dto.CommentModifyReqDto;
import com.carumuch.capstone.comment.dto.CommentReqDto;
import com.carumuch.capstone.comment.repository.CommentRepository;
import com.carumuch.capstone.user.domain.User;
import com.carumuch.capstone.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public Long writeComment(CommentReqDto commentReqDto){
        /*로그인한 유저정보 조회*/
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findLoginUserByLoginId(loginId);

        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));

        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .board(board)
                .commentContent(commentReqDto.getCommentContent())
                .build());

        return comment.getId();

    }
    @Transactional
    public Long modifyComment(Long id, CommentModifyReqDto commentModifyReqDto){
        /*로그인한 유저정보 조회*/
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findLoginUserByLoginId(loginId);

        Comment savedComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        if(!savedComment.getUser().equals(currentUser)){
            throw new RuntimeException("이 게시글을 수정할 권한이 없습니다");
        }

        savedComment.updateComment(commentModifyReqDto.getCommentContent());

        return id;

    }

    @Transactional
    public void deleteComment(Long id){
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findLoginUserByLoginId(loginId);

        Comment savedComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다"));

        if(!savedComment.getUser().equals(currentUser)){
            throw new RuntimeException("이 게시글을 수정할 권한이 없습니다");
        }

        commentRepository.delete(savedComment);
    }
}
