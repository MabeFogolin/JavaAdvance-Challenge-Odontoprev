package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.model.Diario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiarioPostResponse {

    private String mensagem;
    private Diario diario;

}
