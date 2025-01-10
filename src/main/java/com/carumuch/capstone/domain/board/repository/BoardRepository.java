package com.carumuch.capstone.domain.board.repository;

import com.carumuch.capstone.domain.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
