package com.carumuch.capstone.community.infrastructure;

import com.carumuch.capstone.community.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
