package ru.practicum.shareit.itemk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.itemk.model.Comment;

import java.util.Collection;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByItemId(Long itemId);
}