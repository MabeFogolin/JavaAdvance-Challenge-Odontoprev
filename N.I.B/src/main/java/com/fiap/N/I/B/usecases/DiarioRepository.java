package com.fiap.N.I.B.usecases;

import com.fiap.N.I.B.domains.Diario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiarioRepository extends JpaRepository<Diario, Long> {

    List<Diario> findByUsuario_CpfUser(String cpfUser);

    Optional<Diario> findRegistroByUsuario_CpfUserAndDataRegistro(String cpfUser, Date dataRegistro);

}

