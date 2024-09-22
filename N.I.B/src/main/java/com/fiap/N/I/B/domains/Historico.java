package com.fiap.N.I.B.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Historico {

    @ManyToOne
    @JoinColumn(name = "fk_user", referencedColumnName = "cpfUser")
    private String fkUser;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String tratamentoHistorico;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String canalHistorico;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String limpezaHistorico;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String carieHistorico;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String ortodonticoHistorico;

    @NotNull
    @Size(max = 1, message = "Um caractere, podendo ser S ou N")
    private String cirurgiaHistorico;

}
