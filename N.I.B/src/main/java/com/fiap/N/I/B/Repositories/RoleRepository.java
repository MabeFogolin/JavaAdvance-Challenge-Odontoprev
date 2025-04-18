package com.fiap.N.I.B.Repositories;

import com.fiap.N.I.B.model.RoleModel;
import com.fiap.N.I.B.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {

    Optional<RoleModel> findByRoleName(RoleName roleName);
}
