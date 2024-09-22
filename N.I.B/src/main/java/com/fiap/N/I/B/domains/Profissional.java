package com.fiap.N.I.B.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class Profissional {

    @NotNull
    @Size(max = 20, message = "Nome deve ter no máximo 20 caracteres")
    private String nomeProfissional;

    @NotNull
    @Size(max = 30, message = "Sobrenome deve ter no máximo 30 caracteres")
    private String sobrenomeProfissional;

    @NotNull
    @Pattern(regexp = "\\d{11}", message = "Telefone deve conter 11 dígitos numéricos")
    private String telefoneProfissional;

    @NotNull
    @Size(max = 20, message = "Tipo de profissional deve ter no máximo 20 caracteres")
    private String tipoProfissional;

    @NotNull
    private Date dataInscricaoProfissional;

    @Id
    @NotNull
    @Size(max = 15, message = "Registro deve ter no máximo 15 caracteres")
    private String registroProfissional;
}
