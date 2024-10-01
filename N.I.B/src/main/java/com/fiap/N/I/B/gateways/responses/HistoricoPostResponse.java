package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.domains.Historico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPostResponse {

    private String mensagem;
    private Historico historico;

}
