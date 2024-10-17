package com.jobs.linkedIn.integration.services;

import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.dto.auth.LoginUserDto;
import com.jobs.linkedIn.dto.auth.RegisterUserDto;
import com.jobs.linkedIn.entities.user.Role;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.RoleRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.interfaces.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class AuthServiceTest {


    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    RegisterUserDto registerUserDto;
    User user;

    @BeforeEach
    void setUp() {
        registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("ahsan.btph33333@gmail.com");
        registerUserDto.setUsername("ahsanrrrrr");
        registerUserDto.setPassword("gaggagagaga");
        registerUserDto.setFirstName("Ahsan");
        registerUserDto.setLastName("Azeem");

        user = new User();
        String password = "fsafsafas";
        user.setEmail("ahsan.bt11111111111@gmail.com");
        user.setUsername("fffffffffffffffff");
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName("something");
        user.setLastName("sgasgsa");

        userRepository.save(user);
        user.setPassword(password);

        Role role = new Role();
        role.setName(UserRoles.ROLE_INDEX + UserRoles.USER);

        Role role1 = new Role();
        role1.setName(UserRoles.ROLE_INDEX + UserRoles.ADMIN);

        if (!roleRepository.existsByName(role.getName())) roleRepository.save(role);
        if (!roleRepository.existsByName(role1.getName())) roleRepository.save(role1);
    }

    @AfterEach()
    void tearDown () {
        userRepository.deleteAll();
    }


    @Test
    @Transactional
    void SHOULD_REGISTER_USER() {
        this.authService.createUser(registerUserDto);
    }

    @Test
    void SHOULD_NOT_REGISTER_USER_WHEN_USERNAME_EXISTS() {
        registerUserDto.setUsername(user.getUsername());

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.createUser(registerUserDto);
        });
    }

    @Test
    void SHOULD_NOT_REGISTER_USER_WHEN_EMAIL_EXISTS() {
        registerUserDto.setEmail(user.getEmail());

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.createUser(registerUserDto);
        });
    }

    @Test
    void SHOULD_LOGIN_USER() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail(user.getEmail());
        loginUserDto.setPassword(user.getPassword());

        Assertions.assertInstanceOf(String.class, this.authService.login(loginUserDto));
    }

    @Test
    void SHOULD_NOT_LOGIN_WHEN_USER_DOES_NOT_EXIST() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail("fsafsafsa");
        loginUserDto.setPassword(user.getPassword());

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.login(loginUserDto);
        });
    }

    @Test
    void SHOULD_NOT_LOGIN_WHEN_PASSWORD_INCORRECT() {
        LoginUserDto loginUserDto = new LoginUserDto();
        loginUserDto.setEmail(user.getEmail());
        loginUserDto.setPassword("some random password");

        Assertions.assertThrows(ApiException.class, () -> {
            this.authService.login(loginUserDto);
        });
    }
}
