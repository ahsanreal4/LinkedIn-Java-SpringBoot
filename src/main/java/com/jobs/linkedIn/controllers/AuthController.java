package com.jobs.linkedIn.controllers;

import com.jobs.linkedIn.dto.auth.RegisterUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.auth.LoginUserDto;
import com.jobs.linkedIn.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "endpoints to login and signup and also get logged in user profile"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService)  {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto userDto = this.authService.createUser(registerUserDto);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        String token = this.authService.login(loginUserDto);

        return ResponseEntity.ok(token);
    }
}
