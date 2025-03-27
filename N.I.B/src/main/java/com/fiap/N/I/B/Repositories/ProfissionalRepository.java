package com.fiap.N.I.B.Repositories;

import com.fiap.N.I.B.model.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.List;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, String> {
    Optional<Profissional> findProfissionalByRegistroProfissional(String registroProfissional);
    List<Profissional> findUsuariosByTipoProfissional(String tipoProfissional);
    Page<Profissional> findUsuariosByTipoProfissional(String tipoProfissional, Pageable pageable);

    @Procedure(procedureName = "consultas.deletar_profissional_procedure")
    void deletarProfissionalProcedure(String registroProfissional);

    @Procedure(procedureName = "consultas.ler_profissional")
    Object[] lerProfissionalProcedure(String registroProfissional);
}
