package com.jobs.linkedIn.config;

import com.jobs.linkedIn.constants.UserRoles;
import com.jobs.linkedIn.entities.user.Role;
import com.jobs.linkedIn.entities.user.User;
import com.jobs.linkedIn.repositories.user.RoleRepository;
import com.jobs.linkedIn.repositories.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String admin = UserRoles.ROLE_INDEX + UserRoles.ADMIN;
        String user = UserRoles.ROLE_INDEX + UserRoles.USER;

        Role adminRole = roleRepository.findByName(admin);
        Role userRole = roleRepository.findByName(user);

        // Create admin
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(admin);
            roleRepository.save(adminRole);

            User user1 = new User();
            user1.setFirstName("Ahsan");
            user1.setLastName("Azeem");
            user1.setPassword(passwordEncoder.encode("axeem0099"));
            user1.setUsername("ahsanreal4");
            user1.setEmail("ahsan.btph123@gmail.com");
            user1.getRoles().add(adminRole);
            userRepository.save(user1);
        }

        // Create user
        if (userRole == null) {
            userRole = new Role();
            userRole.setName(user);
            roleRepository.save(userRole);

            User user2 = new User();
            user2.setFirstName("Aden");
            user2.setLastName("Azeem");
            user2.setPassword(passwordEncoder.encode("axeem0099"));
            user2.setUsername("adenreal4");
            user2.setEmail("ahsan.btph234@gmail.com");
            user2.getRoles().add(userRole);
            userRepository.save(user2);
        }
    }
}
