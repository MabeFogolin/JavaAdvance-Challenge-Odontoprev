package com.fiap.N.I.B.usecases.Endereco;

import com.fiap.N.I.B.domains.Endereco;
import com.fiap.N.I.B.gateways.requests.EnderecoPatch;

import java.util.Optional;

public interface EnderecoAtualiarParcial {

    Optional<Endereco> atualizarParcial(String idPessoa, EnderecoPatch enderecoPatch);

}
