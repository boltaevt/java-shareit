package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapperTest {

    @Test
    public void testToCommentDto() {
        Comment comment = Comment.builder()
                .id(1L)
                .text("Test comment")
                .author(User.builder().name("John Doe").build())
                .created(LocalDateTime.now())
                .build();

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        Assertions.assertEquals(comment.getId(), commentDto.getId());
        Assertions.assertEquals(comment.getText(), commentDto.getText());
        Assertions.assertEquals(comment.getAuthor().getName(), commentDto.getAuthorName());
        Assertions.assertEquals(comment.getCreated(), commentDto.getCreated());
    }

    @Test
    public void testToComment() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Test comment")
                .created(LocalDateTime.now())
                .build();

        Comment comment = CommentMapper.toComment(commentDto);

        Assertions.assertEquals(commentDto.getId(), comment.getId());
        Assertions.assertEquals(commentDto.getText(), comment.getText());
        Assertions.assertEquals(commentDto.getCreated(), comment.getCreated());
    }
}
