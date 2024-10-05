package com.fiap.N.I.B.gateways.requests;

import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.domains.Usuario;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoPatch {

    @Null
    @Size(max = 25, message = "Rua deve ter no máximo 25 caracteres")
    private String ruaEndereco;

    @Nullable
    private Integer numeroEndereco;

    @Null
    @Size(max = 20, message = "Complemento deve ter no máximo 20 caracteres")
    private String complementoEndereco;

    @Null
    @Size(max = 20, message = "Bairro deve ter no máximo 20 caracteres")
    private String bairroEndereco;

    @Null
    @Size(max = 30, message = "Cidade deve ter no máximo 30 caracteres")
    private String cidadeEndereco;

    @Null
    @Size(max = 9, message = "CEP deve ter no máximo 9 caracteres")
    private String cepEndereco;

    @Null
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estadoEndereco;

    @Null
    private Usuario usuario;

    @Null
    private Profissional profissional;


}
