package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ItemRepository itemRepository;

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

        items.get(0).setRequest(requests.get(0));
        items.get(1).setRequest(requests.get(1));
        items.get(2).setRequest(requests.get(2));
        items.forEach(testEntityManager::persist);

        requests.get(0).setRequestor(users.get(1));
        requests.get(1).setRequestor(users.get(2));
        requests.get(2).setRequestor(users.get(2));
        requests.forEach(testEntityManager::persist);
    }

    @Test
    void findAllByOwner() {
        List<Item> itemsByOwner = itemRepository.findAllByOwner(users.get(2), PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(0, itemsByOwner.size());

        itemsByOwner = itemRepository.findAllByOwner(users.get(0), PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(1, itemsByOwner.size());
        Assertions.assertEquals("this is item1", itemsByOwner.get(0).getDescription());

        itemsByOwner = itemRepository.findAllByOwner(users.get(1), PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(2, itemsByOwner.size());
        Assertions.assertEquals("this is item3", itemsByOwner.get(1).getDescription());
    }

    @Test
    void findAllByRequest() {
        List<Item> itemsByRequest = new ArrayList<>(itemRepository.findAllByRequest(requests.get(2)));
        Assertions.assertEquals(1, itemsByRequest.size());
        Assertions.assertEquals("this is item3", itemsByRequest.get(0).getDescription());
    }

    @Test
    void search() {
        List<Item> foundItems = itemRepository.search("unknown", PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(0, foundItems.size());

        foundItems = itemRepository.search(" item1", PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(1, foundItems.size());
        Assertions.assertEquals("this is item1", foundItems.get(0).getDescription());

        foundItems = itemRepository.search(" item", PageRequest.of(0, 3)).toList();
        Assertions.assertEquals(3, foundItems.size());
        Assertions.assertEquals("this is item3", foundItems.get(2).getDescription());
    }
}