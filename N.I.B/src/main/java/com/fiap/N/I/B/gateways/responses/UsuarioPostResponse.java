package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.domains.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPostResponse extends RepresentationModel<UsuarioPostResponse> {

    private String mensagem;
    private Usuario usuario;


}
