package com.fiap.N.I.B.gateways.Repositories;

import com.fiap.N.I.B.domains.Historico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoricoRepository extends JpaRepository<Historico, String> {

    Optional<Historico> findByUsuario_CpfUser(String cpfUser);

    //Optional<Historico> findById(Long idDiario);

}
