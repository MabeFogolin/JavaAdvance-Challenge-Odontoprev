package com.fiap.N.I.B.usecases;

import com.fiap.N.I.B.domains.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, String> {
}
