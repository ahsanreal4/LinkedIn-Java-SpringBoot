package com.jobs.linkedIn.services.impl;

import com.jobs.linkedIn.config.security.JwtTokenProvider;
import com.jobs.linkedIn.dto.user.UpdateUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.user.UserProfileDto;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           JwtTokenProvider jwtTokenProvider,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = this.userRepository.findAll();

        return users.stream().map(curr -> modelMapper.map(curr, UserDto.class)).toList();
    }

    @Override
    public String deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist with id " + id)
        );

        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    public UserDto updateUser(UpdateUserDto updateUserDto, String token) {
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        if (updateUserDto.getUsername() != null && !updateUserDto.getUsername().equals(user.getUsername())) {
            String username = updateUserDto.getUsername();

            if(userRepository.findByUsername(username).isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, "user already exists with this username");

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
    public UserProfileDto getProfile(String token) {
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        UserProfileDto userProfileDto = modelMapper.map(user, UserProfileDto.class);
        if(user.getUserInfo() != null) modelMapper.map(user.getUserInfo(), userProfileDto);

        return userProfileDto;
    }

    @Override
    public List<UserDto> getUserFriends(String token) {
        String email = jwtTokenProvider.getEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user does not exist"));

        List<User> friends = user.getFriends().stream().toList();

        return friends.stream().map(friend -> modelMapper.map(friend, UserDto.class)).collect(Collectors.toList());
    }
}
