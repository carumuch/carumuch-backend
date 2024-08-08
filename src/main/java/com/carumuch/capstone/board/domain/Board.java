package com.carumuch.capstone.board.domain;

import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
import com.carumuch.capstone.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board")
@Getter
public class Board extends BaseCreateByEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "boardTitle")
    private String boardTitle;

    @Column(name  = "boardContent")
    private String boardContent;

    @Column(name = "boardHits")
    private int boardHits;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = LAZY)
    private List<BoardImage> boardImageList = new ArrayList<>();


    @Builder
    public Board(User user,String boardTitle,String boardContent,int boardHits) {
        this.user = user;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardHits = boardHits;
        user.getBoards().add(this);
    }

    public void updateBoard(String boardTitle, String boardContent){
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }


}
