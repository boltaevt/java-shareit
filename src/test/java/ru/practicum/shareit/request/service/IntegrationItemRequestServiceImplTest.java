package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class IntegrationItemRequestServiceImplTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;

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
        users.forEach(em::persist);

        items.get(0).setOwner(users.get(0));
        items.get(1).setOwner(users.get(1));
        items.get(2).setOwner(users.get(1));
        items.forEach(em::persist);

        requests.get(0).setRequestor(users.get(1));
        requests.get(1).setRequestor(users.get(2));
        requests.get(2).setRequestor(users.get(2));
        requests.forEach(em::persist);
    }

    @Test
    void getItemRequests() {
        List<ItemRequestDto> serviceItemRequests = new ArrayList<>(itemRequestService.getItemRequests(users.get(2).getId()));

        assertThat(serviceItemRequests, hasSize(2));

        for (int i = 0; i < serviceItemRequests.size(); i++) {
            assertThat(serviceItemRequests.get(i).getId(), notNullValue());
            assertThat(serviceItemRequests.get(i).getDescription(), equalTo(requests.get(i + 1).getDescription()));
        }
    }

    @Test
    void getOtherItemRequests() {
        List<ItemRequestDto> serviceItemRequests = new ArrayList<>(itemRequestService.getOtherItemRequests(users.get(0).getId(), 0L, 5L));

        assertThat(serviceItemRequests, hasSize(3));

        List<ItemRequest> sortedRequests = requests.stream()
                .sorted(Comparator.comparing(ItemRequest::getCreated).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < serviceItemRequests.size(); i++) {
            assertThat(serviceItemRequests.get(i).getId(), notNullValue());
            assertThat(serviceItemRequests.get(i).getDescription(), equalTo(sortedRequests.get(i).getDescription()));
        }
    }
}