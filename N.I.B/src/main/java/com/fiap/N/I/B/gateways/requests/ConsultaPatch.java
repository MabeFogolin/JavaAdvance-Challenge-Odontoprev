package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaPatch {

    @Null
    private Date dataConsulta;

    @Null
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    private String descricaoConsulta;
}
