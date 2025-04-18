package com.fiap.N.I.B.configs;

import com.fiap.N.I.B.Repositories.RoleRepository;
import com.fiap.N.I.B.model.RoleModel;
import com.fiap.N.I.B.model.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<RoleName> roles = List.of(RoleName.ROLE_USER);
        for (RoleName roleName : roles) {
            roleRepository.findByRoleName(roleName).orElseGet(() -> {
                RoleModel role = new RoleModel();
                role.setRoleName(roleName);
                return roleRepository.save(role);
            });
        }
    }
}
