package com.fiap.N.I.B.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diario extends RepresentationModel<Diario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
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
    @JsonIgnore
    private Usuario usuario;
}
