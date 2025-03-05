package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.model.Endereco;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnderecoPostResponse {

    @NotNull
    private String mensagem;

    private Endereco endereco;


}
