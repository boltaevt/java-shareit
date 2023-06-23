package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }

    private final List<User> users = List.of(
            User.builder().name("user1").email("user1@mail.ru").build(),
            User.builder().name("user2").email("user2@mail.ru").build(),
            User.builder().name("user3").email("user3@mail.ru").build());
    private final List<Item> items = List.of(
            Item.builder().name("item1").description("this is item1").available(true).build(),
            Item.builder().name("item2").description("this is item2").available(true).build(),
            Item.builder().name("item3").description("this is item3").available(true).build());

    private final List<Comment> comments = List.of(
            Comment.builder().text("comment1").build(),
            Comment.builder().text("comment2").build(),
            Comment.builder().text("comment3").build());

    @BeforeEach
    public void installEntities() {
        users.forEach(testEntityManager::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(testEntityManager::persist);

        comments.get(0).setItem(items.get(0));
        comments.get(1).setItem(items.get(1));
        comments.get(2).setItem(items.get(1));

        comments.get(0).setAuthor(users.get(2));
        comments.get(1).setAuthor(users.get(2));
        comments.get(2).setAuthor(users.get(2));
        comments.forEach(testEntityManager::persist);
    }

    @Test
    void findAllByItemId() {
        List<Comment> foundComments = new ArrayList<>(commentRepository.findAllByItemId(items.get(2).getId()));
        Assertions.assertEquals(0, foundComments.size());

        foundComments = new ArrayList<>(commentRepository.findAllByItemId(items.get(1).getId()));
        Assertions.assertEquals(2, foundComments.size());
        Assertions.assertEquals("comment3", foundComments.get(1).getText());
    }
}