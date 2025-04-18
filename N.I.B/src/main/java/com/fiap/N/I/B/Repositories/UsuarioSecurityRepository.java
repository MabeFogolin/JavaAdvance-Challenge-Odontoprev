package com.fiap.N.I.B.Repositories;

import com.fiap.N.I.B.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioSecurityRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByCpfUser(String cpf);
    Optional<Usuario> findByEmailUser(String email);
}