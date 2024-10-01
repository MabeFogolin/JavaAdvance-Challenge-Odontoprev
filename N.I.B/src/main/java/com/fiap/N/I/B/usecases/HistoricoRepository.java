package com.fiap.N.I.B.usecases;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.domains.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HistoricoRepository extends JpaRepository<Historico, String> {

    Optional<Historico> findByUsuario_CpfUser(String cpfUser);

    Optional<Historico> findById(Long idDiario);

}
