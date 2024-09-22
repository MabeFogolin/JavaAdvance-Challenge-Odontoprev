package com.fiap.N.I.B.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @NotNull
    @CPF(message ="CPF deve conter 11 dígitos numéricos" )
    private String cpfUser;

    @NotNull
    @Size(max = 30, message = "Nome deve ter no máximo 30 caracteres")
    private String nomeUser;

    @NotNull
    @Size(max = 30, message = "Sobrenome deve ter no máximo 30 caracteres")
    private String sobrenomeUser;

    @NotNull
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 a 11 dígitos")
    private String telefoneUser;

    @NotNull
    private Date dataNascimentoUser;

    @NotNull
    @Size(max = 20, message = "Tipo de plano deve ter no máximo 20 caracteres")
    private String planoUser;

    @NotNull
    @Email(message = "Informe um e-mail válido")
    @Size(max = 50, message = "Email deve ter no máximo 50 caracteres")
    private String emailUser;
}
