package com.jobs.linkedIn.repository.user;

import com.jobs.linkedIn.entities.user.Role;
import com.jobs.linkedIn.repositories.user.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    void SHOULD_SAVE() {
        Role role = new Role();
        role.setName("ADMIN");

        Role savedRole = roleRepository.save(role);

        Assertions.assertNotSame(savedRole, null);
    }

    @Test
    void SHOULD_NOT_SAVE_DUPLICATE_NAME() {
        Role role = new Role();
        role.setName("ADMIN");

        Role role1 = new Role();
        role1.setName("ADMIN");

        roleRepository.save(role);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            roleRepository.save(role1);
        });
    }
}
