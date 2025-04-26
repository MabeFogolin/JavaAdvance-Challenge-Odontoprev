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
@Table(name = "Profissional") // Garante que a entidade está mapeada corretamente para a tabela no banco
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "deletarProfissional",
                procedureName = "consultas.deletar_profissional",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_registro_profissional", type = String.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "lerProfissionalProcedure",
                procedureName = "consultas.ler_profissional",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_registro_profissional", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "v_nome_profissional", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "v_sobrenome_profissional", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "v_telefone_profissional", type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "v_data_inscricao_profissional", type = Date.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "v_email_profissional", type = String.class)
                }
        )
})

public class Profissional extends RepresentationModel<Profissional> {

    @NotNull
    @Size(max = 20, message = "Nome deve ter no máximo 20 caracteres")
    @Column(name = "nome_profissional", length = 20, nullable = false)
    private String nomeProfissional;

    @NotNull
    @Size(max = 30, message = "Sobrenome deve ter no máximo 30 caracteres")
    @Column(name = "sobrenome_profissional", length = 30, nullable = false)
    private String sobrenomeProfissional;

    @NotNull
    @Column(name = "telefone_profissional", nullable = false)
    private String telefoneProfissional;

    @NotNull
    @Size(max = 20, message = "Tipo de profissional deve ter no máximo 20 caracteres")
    @Column(name = "tipo_profissional", length = 20, nullable = false)
    private String tipoProfissional;

    @NotNull
    @Column(name = "data_inscricao_profissional", nullable = false)
    private Date dataInscricaoProfissional;

    @Id
    @NotNull
    @Size(max = 15, message = "Registro deve ter no máximo 15 caracteres")
    @Column(name = "registro_profissional", length = 15, nullable = false)
    private String registroProfissional;

    @NotNull
    @Email(message = "Informe um e-mail válido")
    @Column(name = "email_profissional", length = 50, nullable = false)
    private String emailProfissional;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    @OneToOne(mappedBy = "profissional", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Endereco endereco;

    public void adicionarConsulta(Consulta consulta) {
        consulta.setProfissional(this);
        this.consultas.add(consulta);
    }

}
