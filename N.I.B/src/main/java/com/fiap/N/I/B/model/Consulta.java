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
@Table(name = "Consulta")
public class Consulta extends RepresentationModel<Consulta> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @NotNull
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    @Column(name = "descricao_consulta", nullable = false, length = 150)
    private String descricaoConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "fk_user", referencedColumnName = "cpf_user", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "fk_profissional", referencedColumnName = "registro_profissional", nullable = false)
    private Profissional profissional;
}
