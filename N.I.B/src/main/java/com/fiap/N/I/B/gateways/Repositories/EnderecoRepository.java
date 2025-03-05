package com.fiap.N.I.B.gateways.Repositories;

import com.fiap.N.I.B.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

    Optional<Endereco> findByUsuario_CpfUser(String cpfUser);

    Optional<Endereco> findByProfissional_RegistroProfissional(String registroProfissional);

}
