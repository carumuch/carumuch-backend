package com.carumuch.capstone.comment.repository;

import com.carumuch.capstone.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
