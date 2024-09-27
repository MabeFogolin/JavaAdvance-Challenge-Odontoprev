package com.fiap.N.I.B.domains;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Usuario usuario; // Alterado para referenciar o objeto Usuario

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
