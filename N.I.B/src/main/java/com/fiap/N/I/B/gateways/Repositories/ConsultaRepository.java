package com.fiap.N.I.B.gateways.Repositories;

import com.fiap.N.I.B.domains.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, String>{

    List<Consulta> findByUsuario_CpfUser(String cpfUser);

    Optional<Consulta> findConsultaByUsuario_CpfUserAndDataConsulta(String cpfUser, LocalDate dataConsulta);

    List<Consulta> findConsultaByProfissional_RegistroProfissional(String  registroProfissional);

    Optional<Consulta> findConsultaByProfissional_RegistroProfissionalAndUsuario_CpfUserAndDataConsulta(String cpfUser, String registroProfissional, LocalDate dataConsulta);


}
