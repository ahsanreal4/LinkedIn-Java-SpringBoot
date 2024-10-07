package com.jobs.linkedIn.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserFriendsDto {
    List<UserDto> userFriends;
}
