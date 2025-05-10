package com.fiap.N.I.B.Repositories;

import com.fiap.N.I.B.model.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserSecurity, Long> {
    Optional<UserSecurity> findByUsername(String username);
}