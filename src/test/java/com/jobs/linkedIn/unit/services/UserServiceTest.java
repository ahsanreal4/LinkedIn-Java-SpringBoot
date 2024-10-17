package com.jobs.linkedIn.unit.services;


import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.user.UserProfileDto;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.impl.UserServiceImpl;
import com.jobs.linkedIn.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserUtils userUtils;


    User getNewUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("ahsan.bt333@gmail.com");
        user.setUsername("safsafsa");
        user.setPassword("axeem0099");
        user.setFirstName("Ahsan");
        user.setLastName("Azeem");

        return user;
    }

    UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());

        return userDto;
    }

    UpdateUserDto getUserUpdateDto() {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setUsername("ahsanreal4");
        updateUserDto.setFirstName("Ahsan");
        updateUserDto.setLastName("Azeem");
        updateUserDto.setPassword("axeem0099");

        return updateUserDto;
    }

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void SHOULD_RETURN_USERS() {
        User user = getNewUser();
        User user2 = getNewUser();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user2);

        UserDto userDto = getUserDto(user);

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> returnedUserss = userService.getUsers();

        Assertions.assertEquals(2, returnedUserss.size());
    }

    @Test
    void SHOULD_DELETE_USER() {
        User user = getNewUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        String response = "User deleted successfully";

        Assertions.assertSame(response, userService.deleteUser(user.getId()));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void SHOULD_UPDATE_USER() {
        String email = "ahsan.btph123@gmail.com";

        when(userUtils.getEmail()).thenReturn(email);

        User user = getNewUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        UpdateUserDto updateUserDto = getUserUpdateDto();
        updateUserDto.setPassword(null);
        UserDto userDto = getUserDto(user);

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        Assertions.assertSame(userDto, this.userService.updateUser(updateUserDto));
    }

    @Test
    void SHOULD_NOT_UPDATE_ON_DUPLICATE_USERNAME() {
        String email = "ahsan.btph123@gmail.com";
        String username = "ahsanreal4";

        when(userUtils.getEmail()).thenReturn(email);

        User user = getNewUser();
        user.setUsername("fsafsa");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UpdateUserDto updateUserDto = getUserUpdateDto();
        updateUserDto.setPassword(null);
        updateUserDto.setUsername(username);

        UserDto userDto = getUserDto(user);

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        Assertions.assertThrows(ApiException.class, () -> {
            this.userService.updateUser(updateUserDto);
        });
    }

    @Test
    void SHOULD_RETURN_USER_PROFILE() {
        User user = getNewUser();
        UserProfileDto userProfileDto = new UserProfileDto();
        String email = "ahsan.btph123@gmail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userUtils.getEmail()).thenReturn(email);
        when(modelMapper.map(user, UserProfileDto.class)).thenReturn(userProfileDto);

        Assertions.assertSame(userProfileDto, this.userService.getProfile());
    }

    @Test
    void SHOULD_RETURN_USER_FRIENDS() {
        User user = getNewUser();
        String email = "ahsan.btph123@gmail.com";

        Set<User> friends = new HashSet<>();
        friends.add(getNewUser());
        friends.add(getNewUser());
        user.setFriends(friends);
        UserDto userDto = getUserDto(user);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userUtils.getEmail()).thenReturn(email);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);


        List<UserDto> friendDtos = this.userService.getUserFriends();

        Assertions.assertEquals(2, friendDtos.size());
    }
}
