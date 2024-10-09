package com.jobs.linkedIn.repository.user;

import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.repositories.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertNotSame;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    User user = new User();
    User user2 = new User();

    @BeforeEach
    public void setUp() {
        user.setUsername("ahsanreal5315314");
        user.setEmail("ahsan.btph12353153135@gmail.com");
        user.setPassword("axeem0099");
        user.setFirstName("Ahsan");
        user.setLastName("Azeem");

        user2.setUsername("ahsanreal5");
        user2.setEmail("ahsan.btph1234@gmail.com");
        user2.setPassword("axeem0099");
        user2.setFirstName("Ahsan");
        user2.setLastName("Azeem");
    }

    @Test
    void SHOULD_SAVE_USER() {
        User savedUser = userRepository.save(user);

        assertNotSame(savedUser, null);
    }

    @Test
    void SHOULD_FIND_USERS() {
        assertNotSame(userRepository.findByUsernameOrEmail(user.getUsername(), null), null);
        assertNotSame(userRepository.findByUsernameOrEmail(null, user2.getEmail()), null);
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_USERNAME() {
        String username = "ahsanreal33334";
        user.setUsername(username);
        user2.setUsername(username);

        userRepository.save(user);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_EMAIL() {
        String email = "ahsan.btph12311111@gmail.com";
        user.setEmail(email);
        user2.setEmail(email);

        userRepository.save(user);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });
    }


}
