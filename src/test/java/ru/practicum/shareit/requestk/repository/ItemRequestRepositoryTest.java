package ru.practicum.shareit.requestk.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.itemk.model.Item;
import ru.practicum.shareit.requestk.model.ItemRequest;
import ru.practicum.shareit.userk.model.User;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

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

    private final List<ItemRequest> requests = List.of(
            ItemRequest.builder().description("this is request1").build(),
            ItemRequest.builder().description("this is request2").build(),
            ItemRequest.builder().description("this is request3").build());

    @BeforeEach
    public void installEntities() {
        users.forEach(testEntityManager::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(testEntityManager::persist);

        requests.get(0).setRequestor(users.get(1));
        requests.get(1).setRequestor(users.get(2));
        requests.get(2).setRequestor(users.get(2));
        requests.forEach(testEntityManager::persist);
    }

    @Test
    void findAllByRequestor() {
        List<ItemRequest> requestsByRequestor = new ArrayList<>(itemRequestRepository.findAllByRequestor(users.get(0)));
        Assertions.assertEquals(0, requestsByRequestor.size());

        requestsByRequestor = new ArrayList<>(itemRequestRepository.findAllByRequestor(users.get(1)));
        Assertions.assertEquals(1, requestsByRequestor.size());
        Assertions.assertEquals("this is request1", requestsByRequestor.get(0).getDescription());

        requestsByRequestor = new ArrayList<>(itemRequestRepository.findAllByRequestor(users.get(2)));
        Assertions.assertEquals(2, requestsByRequestor.size());
        Assertions.assertEquals("this is request3", requestsByRequestor.get(1).getDescription());
    }

    @Test
    void findAllByRequestorIdNot() {
        List<ItemRequest> requestsByRequestor = itemRequestRepository.findAllByRequestorIdNot(users.get(0).getId(), PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(3, requestsByRequestor.size());
        Assertions.assertEquals("this is request2", requestsByRequestor.get(1).getDescription());
    }
}