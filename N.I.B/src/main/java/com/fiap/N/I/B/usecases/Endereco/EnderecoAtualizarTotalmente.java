package com.fiap.N.I.B.usecases.Endereco;

import com.fiap.N.I.B.domains.Endereco;

import java.util.Optional;

public interface EnderecoAtualizarTotalmente {

    Optional<Endereco> atualizarTotalmente(String idPessoa, Endereco endereco);

}
