package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.domains.Consulta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsultaPostResponse {

    private String mensagem;

    private Consulta consulta;

}
