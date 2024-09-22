package com.fiap.N.I.B.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diario {

    @NotNull
    private Date dataDiario;

    @NotNull
    @Max(99)
    private int escovacaoDiario;

    @NotNull
    @Max(99)
    private int usoFioDiario;

    @NotNull
    @Max(99)
    private int usoEnxaguanteDiario;

    @NotNull
    @Size(max = 30, message = "Sintoma deve ter no máximo 30 caracteres")
    private String sintomaDiario;

    @ManyToOne
    @JoinColumn(name = "fk_user", referencedColumnName = "cpfUser")
    private Usuario usuario;
}
