package com.fiap.N.I.B.ignore.usecases.Endereco;

import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.gateways.requests.EnderecoPatch;

import java.util.Optional;

public interface EnderecoAtualiarParcial {

    Optional<Endereco> atualizarParcial(String idPessoa, EnderecoPatch enderecoPatch);

}
