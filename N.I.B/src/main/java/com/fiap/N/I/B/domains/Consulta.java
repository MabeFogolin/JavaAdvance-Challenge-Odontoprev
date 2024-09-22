package com.fiap.N.I.B.domains;

import jakarta.persistence.*;
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
public class Consulta {

    @NotNull
    private Date dataConsulta;

    @NotNull
    @Size(max = 150, message = "Descrição deve ter no máximo 150 caracteres")
    private String descricaoConsulta;

    @ManyToOne
    @JoinColumn(name = "fk_user", referencedColumnName = "cpfUser")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_profissional", referencedColumnName = "registroProfissional")
    private Profissional profissional;
}
