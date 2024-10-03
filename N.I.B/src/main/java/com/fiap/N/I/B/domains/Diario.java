package com.fiap.N.I.B.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
//    @Temporal(TemporalType.DATE)
    private LocalDate dataRegistro;

    @NotNull
    @Max(99)
    private int escovacaoDiario;

    @NotNull
    @Max(99)
    private int usoFioDiario;

    @NotNull
    @Max(99)
    private int usoEnxaguanteDiario;


    @Size(max = 30, message = "Sintoma deve ter no máximo 30 caracteres")
    private String sintomaDiario;

    @ManyToOne
    private Usuario usuario;
}
