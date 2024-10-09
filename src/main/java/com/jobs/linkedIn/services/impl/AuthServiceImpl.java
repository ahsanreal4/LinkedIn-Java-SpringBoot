package com.jobs.linkedIn.services.impl;

import com.jobs.linkedIn.config.security.JwtTokenProvider;
import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.dto.auth.RegisterUserDto;
import com.jobs.linkedIn.dto.user.UserDto;
import com.jobs.linkedIn.dto.auth.LoginUserDto;
import com.jobs.linkedIn.entities.user.Role;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.exception.ApiException;
import com.jobs.linkedIn.repositories.user.RoleRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import com.jobs.linkedIn.services.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           ModelMapper mapper,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDto createUser(RegisterUserDto registerUserDto) {
        Optional<User> user = userRepository.findByUsername(registerUserDto.getUsername());

        if (user.isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, "username already exists");

        Optional<User> user2 = userRepository.findByEmail(registerUserDto.getEmail());

        if (user2.isPresent()) throw new ApiException(HttpStatus.BAD_REQUEST, "email already exists");

        User userToSave = mapper.map(registerUserDto, User.class);
        userToSave.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(UserRoles.ROLE_INDEX + UserRoles.USER);

        if (userRole == null) throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "user role not found");

        roles.add(userRole);
        userToSave.setRoles(roles);

        User createdUser = userRepository.save(userToSave);

        return mapper.map(createdUser, UserDto.class);
    }

    @Override
    public String login(LoginUserDto loginUserDto) {
        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user with this email doesn't exist"));

        if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid Credentials");

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(),
                loginUserDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }
}
