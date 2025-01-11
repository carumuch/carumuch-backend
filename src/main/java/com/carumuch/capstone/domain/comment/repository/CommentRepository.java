package com.carumuch.capstone.domain.comment.repository;

import com.carumuch.capstone.domain.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
