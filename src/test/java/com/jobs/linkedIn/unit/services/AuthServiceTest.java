package com.jobs.linkedIn.unit.services;


import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.dto.auth.RegisterUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.entities.user.Role;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.RoleRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;


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

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void SHOULD_REGISTER_USER() {
        User user = getNewUser();
        UserDto userDto = getUserDto(user);
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(user.getUsername());
        registerUserDto.setEmail(user.getEmail());
        registerUserDto.setPassword("axeem0099");
        Role role = new Role();
        role.setName(UserRoles.ROLE_INDEX + UserRoles.USER);

        when(modelMapper.map(registerUserDto, User.class)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("some password");
        when(roleRepository.findByName(UserRoles.ROLE_INDEX + UserRoles.USER)).thenReturn(role);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertSame(userDto, this.authService.createUser(registerUserDto));
    }

    @Test
    void SHOULD_NOT_REGISTER_DUPLICATE_USERNAME() {
        User user = getNewUser();
        UserDto userDto = getUserDto(user);
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(user.getUsername());
        registerUserDto.setEmail(user.getEmail());
        registerUserDto.setPassword("axeem0099");
        Role role = new Role();
        role.setName(UserRoles.ROLE_INDEX + UserRoles.USER);

        when(modelMapper.map(registerUserDto, User.class)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("some password");
        when(roleRepository.findByName(UserRoles.ROLE_INDEX + UserRoles.USER)).thenReturn(role);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.createUser(registerUserDto);
        });
    }

    @Test
    void SHOULD_NOT_REGISTER_DUPLICATE_EMAIL() {
        User user = getNewUser();
        UserDto userDto = getUserDto(user);
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername(user.getUsername());
        registerUserDto.setEmail(user.getEmail());
        registerUserDto.setPassword("axeem0099");
        Role role = new Role();
        role.setName(UserRoles.ROLE_INDEX + UserRoles.USER);

        when(modelMapper.map(registerUserDto, User.class)).thenReturn(user);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(registerUserDto.getPassword())).thenReturn("some password");
        when(roleRepository.findByName(UserRoles.ROLE_INDEX + UserRoles.USER)).thenReturn(role);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(userRepository.save(user)).thenReturn(user);

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.createUser(registerUserDto);
        });
    }
}
