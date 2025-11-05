package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioPatch {

//    @NotNull
//    private String planoUser;
    private String nomeUser;
    private String sobrenomeUser;
    private String telefoneUser;
    private String emailUser;

}
