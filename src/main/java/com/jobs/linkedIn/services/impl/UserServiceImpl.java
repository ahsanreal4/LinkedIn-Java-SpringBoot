package com.jobs.linkedIn.services.impl;

import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.user.UserProfileDto;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.entities.user.UserInfo;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.interfaces.UserService;
import com.jobs.linkedIn.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final String USER_NAME_ALREADY_EXISTS = "username already exists";
    private final String USER_DOES_NOT_EXIST = "user does not exist";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserUtils userUtils;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder, UserUtils userUtils) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userUtils = userUtils;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = this.userRepository.findAll();

        return users.stream().map(curr -> modelMapper.map(curr, UserDto.class)).toList();
    }

    @Override
    public String deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, USER_DOES_NOT_EXIST)
        );

        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUserDto) {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        if (updateUserDto.getUsername() != null && !updateUserDto.getUsername().equals(user.getUsername())) {
            String username = updateUserDto.getUsername();

            if(userRepository.findByUsername(username).isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, USER_NAME_ALREADY_EXISTS);

            user.setUsername(username);
        }

        if (updateUserDto.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateUserDto.getPassword());
            user.setPassword(encodedPassword);
        }
        if (updateUserDto.getFirstName() != null) {
            user.setFirstName(updateUserDto.getFirstName());
        }
        if (updateUserDto.getLastName() != null) {
            user.setLastName(updateUserDto.getLastName());
        }

        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserProfileDto getProfile() {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        UserProfileDto userProfileDto = modelMapper.map(user, UserProfileDto.class);
        UserInfo userInfo = user.getUserInfo();
        if(userInfo != null) modelMapper.map(userInfo, userProfileDto);

        return userProfileDto;
    }

    @Override
    public List<UserDto> getUserFriends() {
        String email = userUtils.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        Set<User> friends = user.getFriends();

        if (friends == null) return new ArrayList<>();

        List<User> friendsList = friends.stream().toList();

        return friendsList.stream().map(friend -> modelMapper.map(friend, UserDto.class)).collect(Collectors.toList());
    }
}
