package com.carumuch.capstone.community.domain;

import com.carumuch.capstone.common.domain.AggregateRoot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_image")
@Getter
public class BoardImage extends AggregateRoot<BoardImage> {

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "originalImageName")
    private String originalImageName;

    @Column(name = "savedImageName")
    private String savedImageName;

    @Builder
    public BoardImage(Board board, String originalImageName,String savedImageName){
        this.board = board;
        this.originalImageName = originalImageName;
        this.savedImageName = savedImageName;
        board.getBoardImages().add(this);
    }
}
