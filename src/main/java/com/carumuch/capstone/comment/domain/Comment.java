package com.carumuch.capstone.comment.domain;

import com.carumuch.capstone.board.domain.Board;
import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
import com.carumuch.capstone.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
@Getter
public class Comment extends BaseCreateByEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "commentContent")
    private String commentContent;

    @Builder
    public Comment(User user, Board board, String commentContent) {
        this.user = user;
        this.board = board;
        this.commentContent = commentContent;
        user.getComments().add(this);
        board.getComments().add(this);
    }
    public void updateComment(String commentContent){
        this.commentContent = commentContent;
    }


}
