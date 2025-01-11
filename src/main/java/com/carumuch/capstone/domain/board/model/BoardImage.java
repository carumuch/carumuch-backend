package com.carumuch.capstone.domain.board.model;

import com.carumuch.capstone.global.auditing.BaseCreateByEntity;
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
public class BoardImage extends BaseCreateByEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_image_id")
    Long id;

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
