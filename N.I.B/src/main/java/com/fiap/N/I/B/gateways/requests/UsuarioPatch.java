package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioPatch {

    @NotNull
    private String planoUser;
    @NotNull
    private String emailUser;

}
