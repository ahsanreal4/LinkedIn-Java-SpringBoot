package com.jobs.linkedIn.integration.services;

import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.UserService;
import com.jobs.linkedIn.utils.UserUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private Authentication authentication;

    @MockBean
    private UserUtils userUtils;

    User user;
    User user2;

    @BeforeEach
    public void setUp() {
        user = User.builder().
                email("ahsan.btph12355@gmail.com")
                .firstName("Ahsan")
                .lastName("Azeem")
                .password("axeem0099")
                .username("ahsanreal455")
                .build();

        user2 = User.builder()
                .email("ahsan.btph12344@gmail.com")
                .firstName("Ahsan")
                .lastName("Azeem")
                .password("axeem0099")
                .username("ahsanreal45")
                .build();

        // Set up test data before each test
        userRepository.save(user);
        userRepository.save(user2);
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test
        userRepository.deleteAll();
    }


    @Test
    void SHOULD_RETURN_USERS() {
        Assertions.assertEquals(2, userService.getUsers().size());
    }

    @Test
    void SHOULD_DELETE_USER() {
        userService.deleteUser(user.getId());
    }

    @Test
    void SHOULD_NOT_DELETE_USER() {
        Assertions.assertThrows(ApiException.class, () -> {
            userService.deleteUser(99999L);
        });
    }

    @Test
    void SHOULD_UPDATE_USER_USERNAME() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("something good");

        UserDto userDto = userService.updateUser(updateUserDto);
        Assertions.assertEquals(userDto.getUsername(), updateUserDto.getUsername());
    }

    @Test
    void SHOULD_UPDATE_USER_FIRST_AND_LAST_NAME() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("dummy");
        updateUserDto.setLastName("random");

        UserDto userDto = userService.updateUser(updateUserDto);
        Assertions.assertEquals(userDto.getFirstName(), updateUserDto.getFirstName());
        Assertions.assertEquals(userDto.getLastName(), updateUserDto.getLastName());
    }

    @Test
    void SHOULD_NOT_UPDATE_USER() {
        Mockito.when(userUtils.getEmail()).thenReturn("randomdummy");

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setFirstName("dummy");
        updateUserDto.setLastName("random");

        Assertions.assertThrows(ApiException.class, () -> {
            userService.updateUser(updateUserDto);
        });
    }

    @Test
    void SHOULD_RETURN_USER_PROFILE() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        userService.getProfile();
    }

    @Test
    void SHOULD_NOT_RETURN_USER_PROFILE() {
        Mockito.when(userUtils.getEmail()).thenReturn("RANDOM@gmail.com");

        Assertions.assertThrows(ApiException.class, () -> {
            userService.getProfile();
        });
    }

    @Test
    @Transactional
    void SHOULD_RETURN_USER_FRIENDS() {
        Mockito.when(userUtils.getEmail()).thenReturn(user.getEmail());

        List<UserDto> friends = this.userService.getUserFriends();

        Assertions.assertEquals(0, friends.size());
    }

    @Test
    void SHOULD_NOT_RETURN_USER_FRIENDS() {
        Mockito.when(userUtils.getEmail()).thenReturn("RANDOM@gmail.com");

        Assertions.assertThrows(ApiException.class, () -> {
            userService.getUserFriends();
        });
    }
}
