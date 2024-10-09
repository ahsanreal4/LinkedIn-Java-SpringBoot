package com.jobs.linkedIn.controllers;

import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.user.UserProfileDto;
import com.jobs.linkedIn.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(
        name = "Bear Authentication"
)
@Tag(
        name = "User",
        description = "endpoints to get, update and modify users"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        super();
        this.userService = userService;
    }

    @PreAuthorize(UserRoles.HAS_ROLE_ADMIN)
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize(UserRoles.HAS_ROLE_ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id) {
        String message = userService.deleteUser(id);

        return ResponseEntity.ok(message);
    }

    @PutMapping("")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        UserDto userDto = this.userService.updateUser(updateUserDto);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile() {
        UserProfileDto userProfileDto = this.userService.getProfile();

        return ResponseEntity.ok(userProfileDto);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserDto>> getUserFriends() {
        List<UserDto> friends = this.userService.getUserFriends();

        return ResponseEntity.ok(friends);
    }
}
