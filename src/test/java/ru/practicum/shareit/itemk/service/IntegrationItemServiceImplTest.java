package ru.practicum.shareit.itemk.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.itemk.dto.ItemDto;
import ru.practicum.shareit.itemk.model.Item;
import ru.practicum.shareit.userk.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationItemServiceImplTest {
    private final EntityManager em;
    private final ItemService itemService;

    private final List<User> users = List.of(
            User.builder().name("user1").email("user1@mail.ru").build(),
            User.builder().name("user2").email("user2@mail.ru").build(),
            User.builder().name("user3").email("user3@mail.ru").build());

    private final List<Item> items = List.of(
            Item.builder().name("item1").description("this is item1").available(true).build(),
            Item.builder().name("item2").description("this is item2").available(true).build(),
            Item.builder().name("item3").description("this is item3").available(true).build());

    @BeforeEach
    public void installEntities() {
        users.forEach(em::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(em::persist);
    }

    @Test
    void getAllItems() {
        List<ItemDto> serviceItems = new ArrayList<>(itemService.getAllItems(users.get(1).getId(), 0L, 3L));

        assertThat(serviceItems, hasSize(2));

        for (int i = 0; i < serviceItems.size(); i++) {
            assertThat(serviceItems.get(i).getId(), notNullValue());
            assertThat(serviceItems.get(i).getName(), equalTo(items.get(i + 1).getName()));
            assertThat(serviceItems.get(i).getDescription(), equalTo(items.get(i + 1).getDescription()));
            assertThat(serviceItems.get(i).getAvailable(), equalTo(items.get(i + 1).getAvailable()));
        }
    }

    @Test
    void searchItems() {
        List<ItemDto> serviceItems = new ArrayList<>(itemService.searchItems("is it", 0L, 5L));

        assertThat(serviceItems, hasSize(3));

        for (int i = 0; i < serviceItems.size(); i++) {
            assertThat(serviceItems.get(i).getId(), notNullValue());
            assertThat(serviceItems.get(i).getName(), equalTo(items.get(i).getName()));
            assertThat(serviceItems.get(i).getDescription(), equalTo(items.get(i).getDescription()));
            assertThat(serviceItems.get(i).getAvailable(), equalTo(items.get(i).getAvailable()));
        }
    }
}