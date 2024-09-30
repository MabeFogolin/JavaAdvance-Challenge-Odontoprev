package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioPatch {

    @Null
    @Max(1)
    private int escovacaoDiario;

    @Null
    @Max(1)
    private int usoFioDiario;

    @Null
    @Max(1)
    private int usoEnxaguanteDiario;

    @Null
    @Size(max = 30, message = "Sintoma deve ter no máximo 30 caracteres")
    private String sintomaDiario;
}
