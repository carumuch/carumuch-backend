package com.carumuch.capstone.community.infrastructure;

import com.carumuch.capstone.community.domain.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
