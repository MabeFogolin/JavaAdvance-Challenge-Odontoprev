package com.fiap.N.I.B.ignore.usecases.Endereco;

import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.gateways.responses.EnderecoPostResponse;

public interface EnderecoCriar {

    EnderecoPostResponse criarEndereco(String idPessoa, Endereco endereco);

}
