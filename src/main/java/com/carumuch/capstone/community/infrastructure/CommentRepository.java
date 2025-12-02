package com.carumuch.capstone.community.infrastructure;

import com.carumuch.capstone.community.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
