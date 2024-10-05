package com.fiap.N.I.B.usecases.Endereco;

import com.fiap.N.I.B.domains.Endereco;

import java.util.Optional;

public interface EnderecoPorUsuario {

    Optional<Endereco> buscarEnderecoPorUsuario(String cpfUser);

}
