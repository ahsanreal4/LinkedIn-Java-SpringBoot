package com.jobs.linkedIn.services;

import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.user.UserProfileDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();

    String deleteUser(long id);

    UserDto updateUser(UpdateUserDto updateUserDto);

    UserProfileDto getProfile();

    List<UserDto> getUserFriends();
}
