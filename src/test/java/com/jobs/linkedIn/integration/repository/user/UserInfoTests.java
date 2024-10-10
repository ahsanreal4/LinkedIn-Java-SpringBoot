package com.jobs.linkedIn.integration.repository.user;

import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.entities.user.UserInfo;
import com.jobs.linkedIn.repositories.user.UserInfoRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserInfoTests {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    UserInfo userInfo = new UserInfo();
    UserInfo userInfo2 = new UserInfo();

    @BeforeEach
    public void setUp() {
        User user = new User();
        user.setUsername("ahsanreal5315314");
        user.setEmail("ahsan.btph125315313@gmail.com");
        user.setPassword("axeem0099");
        user.setFirstName("Ahsan");
        user.setLastName("Azeem");

        userRepository.save(user);

        userInfo.setUser(user);
        userInfo.setCity("Lahore");
        userInfo.setCountry("Pakistan");
        userInfo.setDob(LocalDate.now());

        userInfo2.setUser(user);
        userInfo2.setCity("Lahore");
        userInfo2.setCountry("Pakistan");
        userInfo2.setDob(LocalDate.now());
    }

    @Test
    void SHOULD_SAVE() {
        userInfoRepository.save(userInfo);
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_USER() {
        userInfoRepository.save(userInfo);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userInfoRepository.save(userInfo2);
        });
    }
}
