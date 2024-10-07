package com.jobs.linkedIn.utils;

import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.entities.user.Role;

import java.util.List;
import java.util.Set;

public class RoleUtils {
    public boolean isAdmin(Set<Role> roles){
        String adminRole = UserRoles.ROLE_INDEX + UserRoles.ADMIN;

        List<Role> rolesList = roles.stream().toList();

        for (Role role : rolesList) {
            if (role.getName().equalsIgnoreCase(adminRole)) return true;
        }

        return false;
    }
}
