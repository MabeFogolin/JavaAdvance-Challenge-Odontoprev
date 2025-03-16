package com.fiap.N.I.B.ignore.gateways.responses;

import com.fiap.N.I.B.model.Consulta;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConsultaPostResponse {

    private String mensagem;

    private Consulta consulta;

}
