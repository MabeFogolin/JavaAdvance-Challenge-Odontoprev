package com.fiap.N.I.B.usecases.Endereco;

import com.fiap.N.I.B.domains.Endereco;
import com.fiap.N.I.B.gateways.responses.EnderecoPostResponse;

public interface EnderecoCriar {

    EnderecoPostResponse criarEndereco(String idPessoa, Endereco endereco);

}
