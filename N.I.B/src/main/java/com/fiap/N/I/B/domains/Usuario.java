package com.fiap.N.I.B.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Embeddable
public class Usuario extends RepresentationModel<Usuario> {

    @Id
    @NotNull
    @CPF(message = "CPF deve conter 11 dígitos numéricos")
    private String cpfUser;

    @NotNull
    private String nomeUser;

    @NotNull
    private String sobrenomeUser;

    @NotNull
    private String telefoneUser;

    @NotNull
    private LocalDate dataNascimentoUser;

    @NotNull
    @Size(max = 20, message = "Tipo de plano deve ter no máximo 20 caracteres")
    private String planoUser;

    @NotNull
    @Email(message = "Informe um e-mail válido")
    @Size(max = 50, message = "Email deve ter no máximo 50 caracteres")
    private String emailUser;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Diario> diarios;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToOne
    private Historico historico;

    @OneToOne
    private Endereco endereco;

    private int pontos = 0;

    private double nota = 0;

    private int sequenciaDias = 0;
}
