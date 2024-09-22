package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.NotNull;

import java.sql.Date;

public record ConsultaPostRequest(
        @NotNull Date dataConsulta
) {
}
