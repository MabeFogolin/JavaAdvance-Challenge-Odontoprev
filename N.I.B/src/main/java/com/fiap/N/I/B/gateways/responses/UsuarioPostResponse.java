package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.domains.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPostResponse {

    private String mensagem;
    private Usuario usuario;


}
