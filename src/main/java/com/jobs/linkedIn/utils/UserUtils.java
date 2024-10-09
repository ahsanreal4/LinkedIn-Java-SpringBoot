package com.jobs.linkedIn.utils;

import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.entities.user.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class UserUtils {
    public boolean isAdmin(Set<Role> roles){
        String adminRole = UserRoles.ROLE_INDEX + UserRoles.ADMIN;

        List<Role> rolesList = roles.stream().toList();

        for (Role role : rolesList) {
            if (role.getName().equalsIgnoreCase(adminRole)) return true;
        }

        return false;
    }

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }
}
