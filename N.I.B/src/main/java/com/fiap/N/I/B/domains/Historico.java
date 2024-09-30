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
    private Usuario usuario;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int tratamentoHistorico;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int canalHistorico;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int limpezaHistorico;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int carieHistorico;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int ortodonticoHistorico;

    @NotNull
    @Size(max = 3, message = "É necessário um valor inteiro, correspodente a quantidade de ocorrências")
    private int cirurgiaHistorico;
}
