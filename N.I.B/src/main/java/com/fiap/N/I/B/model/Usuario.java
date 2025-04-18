package com.fiap.N.I.B.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.Historico;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuario")
public class Usuario extends RepresentationModel<Usuario> implements Serializable {

    @Id
    @NotNull
    @CPF(message = "CPF deve conter 11 dígitos numéricos")
    @Column(name = "cpf_user", length = 11)
    private String cpfUser;

    @NotNull
    @Size(max = 30, message = "Nome deve ter no máximo 30 caracteres")
    @Column(name = "nome_user", length = 30, nullable = false)
    private String nomeUser;

    @NotNull
    @Size(max = 30, message = "Sobrenome deve ter no máximo 30 caracteres")
    @Column(name = "sobrenome_user", length = 30, nullable = false)
    private String sobrenomeUser;

    @NotNull
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter 10 a 11 dígitos")
    @Column(name = "telefone_user", nullable = false)
    private String telefoneUser;

    @NotNull
    @Column(name = "data_nascimento_user", nullable = false)
    private LocalDate dataNascimentoUser;

    @NotNull
    @Size(max = 20, message = "Tipo de plano deve ter no máximo 20 caracteres")
    @Column(name = "plano_user", length = 20, nullable = false)
    private String planoUser;

    @NotNull
    @Email(message = "Informe um e-mail válido")
    @Size(max = 50, message = "Email deve ter no máximo 50 caracteres")
    @Column(name = "email_user", length = 50, nullable = false)
    private String emailUser;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diario> diarios = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Endereco endereco;

    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Historico historico;

    public void adicionarConsulta(Consulta consulta) {
        consulta.setUsuario(this);
        this.consultas.add(consulta);
    }

    public void adicionarDiario(Diario diario) {
        diario.setUsuario(this);
        this.diarios.add(diario);
    }
}
