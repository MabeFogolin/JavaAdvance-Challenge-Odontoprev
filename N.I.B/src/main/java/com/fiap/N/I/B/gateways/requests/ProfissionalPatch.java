package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfissionalPatch {

    private String telefoneProfissional;

    private String emailProfissional;
}
