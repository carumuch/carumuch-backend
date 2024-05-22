package com.carumuch.capstone.board.domain;

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

    @Builder
    public Board(User user) {
        this.user = user;
        user.getBoards().add(this);
    }
}
