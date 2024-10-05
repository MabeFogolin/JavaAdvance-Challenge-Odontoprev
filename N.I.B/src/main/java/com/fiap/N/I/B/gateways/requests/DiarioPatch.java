package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioPatch {

    @Max(99)
<<<<<<< HEAD
    private int escovacaoDiario;

    @Max(99)
    private int usoFioDiario;

    @Max(99)
    private int usoEnxaguanteDiario;
=======
    private Integer escovacaoDiario; // Alterado para Integer

    @Max(99)
    private Integer usoFioDiario; // Alterado para Integer

    @Max(99)
    private Integer usoEnxaguanteDiario; // Alterado para Integer
>>>>>>> b01f36c61cd71ed13b4d06a5b734bbedca66f4a6

    @Size(max = 30, message = "Sintoma deve ter no máximo 30 caracteres")
    private String sintomaDiario;
}
