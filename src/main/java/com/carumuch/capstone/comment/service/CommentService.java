package com.carumuch.capstone.comment.service;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.board.repository.BoardRepository;
import com.carumuch.capstone.comment.domain.Comment;
import com.carumuch.capstone.comment.dto.CommentModifyReqDto;
import com.carumuch.capstone.comment.dto.CommentReqDto;
import com.carumuch.capstone.comment.repository.CommentRepository;
import com.carumuch.capstone.global.common.ErrorCode;
import com.carumuch.capstone.global.common.exception.CustomException;
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

    /**
     * Create: 댓글 작성
     */
    public Long writeComment(CommentReqDto commentReqDto){
        /*로그인한 유저정보 조회*/
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        Board board = boardRepository.findById(commentReqDto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        Comment comment = commentRepository.save(Comment.builder()
                .user(user)
                .board(board)
                .commentContent(commentReqDto.getCommentContent())
                .build());

        return comment.getId();

    }

    /**
     * Update: 댓글 수정
     */
    @Transactional
    public Long modifyComment(Long id, CommentModifyReqDto commentModifyReqDto){
        /*로그인한 유저정보 조회*/
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        Comment savedComment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!savedComment.getUser().equals(user)){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        savedComment.updateComment(commentModifyReqDto.getCommentContent());

        return id;

    }

    /**
     * Delete: 댓글 삭제
     */
    @Transactional
    public void deleteComment(Long id){
        User user = userRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        Comment savedComment = commentRepository.findById(id)
                .orElseThrow(() ->  new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!savedComment.getUser().equals(user)){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(savedComment);
    }
}
