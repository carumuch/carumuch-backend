package com.carumuch.capstone.community.application;

import com.carumuch.capstone.community.domain.Board;
import com.carumuch.capstone.community.domain.BoardRepository;
import com.carumuch.capstone.community.domain.Comment;
import com.carumuch.capstone.community.presentation.dto.CommentModifyReqDto;
import com.carumuch.capstone.community.presentation.dto.CommentReqDto;
import com.carumuch.capstone.community.domain.CommentRepository;
import com.carumuch.capstone.common.legacy.exception.ErrorCode;
import com.carumuch.capstone.common.legacy.exception.CustomException;
import com.carumuch.capstone.identity.domain.user.User;
import com.carumuch.capstone.identity.domain.user.UserLegacyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserLegacyRepository userLegacyRepository;
    private final BoardRepository boardRepository;

    /**
     * Create: 댓글 작성
     */
    public Long writeComment(CommentReqDto commentReqDto){
        /*로그인한 유저정보 조회*/
        User user = userLegacyRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

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
        User user = userLegacyRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

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
        User user = userLegacyRepository.findLoginUserByLoginId(SecurityContextHolder.getContext().getAuthentication().getName());

        Comment savedComment = commentRepository.findById(id)
                .orElseThrow(() ->  new CustomException(ErrorCode.RESOURCE_NOT_FOUND));

        if(!savedComment.getUser().equals(user)){
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(savedComment);
    }
}
