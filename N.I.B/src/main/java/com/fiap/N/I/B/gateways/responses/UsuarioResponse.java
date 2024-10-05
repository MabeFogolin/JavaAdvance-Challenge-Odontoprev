package com.fiap.N.I.B.gateways.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsuarioResponse {
    private String nomeUser;
    private String sobrenomeUser;
    private String planoUser;
    private String emailUser;

}
