package com.fiap.N.I.B.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate dataConsulta;

    @NotNull
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    private String descricaoConsulta;

    @ManyToOne
    @NotNull
    private Usuario usuario;

    @ManyToOne
    @NotNull
    private Profissional profissional;


}
