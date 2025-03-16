package com.fiap.N.I.B.model;

import com.fiap.N.I.B.ignore.Endereco;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profissional extends RepresentationModel<Profissional> {

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

    @NotNull
    @Email(message = "Informe um e-mail válido")
    private String emailProfissional;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToOne
    private Endereco endereco;

}
