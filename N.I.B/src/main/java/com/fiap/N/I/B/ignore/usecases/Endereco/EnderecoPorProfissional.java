package com.fiap.N.I.B.ignore.usecases.Endereco;

import com.fiap.N.I.B.ignore.Endereco;

import java.util.Optional;

public interface EnderecoPorProfissional {

    Optional<Endereco> buscarEnderecoPorProfissional(String registroProfissional);
}
