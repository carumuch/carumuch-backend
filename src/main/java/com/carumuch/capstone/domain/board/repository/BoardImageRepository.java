package com.carumuch.capstone.domain.board.repository;

import com.carumuch.capstone.domain.board.model.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
