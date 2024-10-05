package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfissionalPatch {

<<<<<<< HEAD
    @NotNull
    private String telefoneProfissional;
    @NotNull
=======

    private String telefoneProfissional;

>>>>>>> 52eb5227fa1e128f997f2972d78a3fef1af20287
    private String emailProfissional;
}
