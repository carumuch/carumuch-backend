package com.carumuch.capstone.board.repository;

import com.carumuch.capstone.board.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
