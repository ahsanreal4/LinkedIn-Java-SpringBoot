package com.jobs.linkedIn.services;

import com.jobs.linkedIn.dto.auth.RegisterUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.auth.LoginUserDto;

public interface AuthService {
    UserDto createUser(RegisterUserDto registerUserDto);

    String login(LoginUserDto loginUserDto);
}
