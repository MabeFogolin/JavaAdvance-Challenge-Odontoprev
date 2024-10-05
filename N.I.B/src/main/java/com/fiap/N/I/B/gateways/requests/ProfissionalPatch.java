package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfissionalPatch {


    private String telefoneProfissional;

    private String emailProfissional;
}
