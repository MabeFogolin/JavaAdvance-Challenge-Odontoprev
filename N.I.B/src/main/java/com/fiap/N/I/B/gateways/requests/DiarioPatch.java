package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.Max;
<<<<<<< HEAD
import jakarta.validation.constraints.Null;
=======
>>>>>>> 52eb5227fa1e128f997f2972d78a3fef1af20287
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiarioPatch {

<<<<<<< HEAD
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
=======
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

>>>>>>> 52eb5227fa1e128f997f2972d78a3fef1af20287
    @Size(max = 30, message = "Sintoma deve ter no máximo 30 caracteres")
    private String sintomaDiario;
}
