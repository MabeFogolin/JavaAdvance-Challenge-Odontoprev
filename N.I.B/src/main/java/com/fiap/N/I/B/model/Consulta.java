package com.fiap.N.I.B.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Consulta extends RepresentationModel<Consulta> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate dataConsulta;

    @NotNull
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    private String descricaoConsulta;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @NotNull
    @JsonIgnore
    private Profissional profissional;


}
