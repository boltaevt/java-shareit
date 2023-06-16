package ru.practicum.shareit.userk.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.userk.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(testEntityManager);
    }

    @Test
    void findByEmail() {
        User user = User.builder()
                .name("user")
                .email("user@mail.ru")
                .build();

        Assertions.assertNull(user.getId());
        testEntityManager.persist(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertTrue(userRepository.findByEmail("unknown@mail.ru").isEmpty());
        Assertions.assertTrue(userRepository.findByEmail("user@mail.ru").isPresent());
    }
}