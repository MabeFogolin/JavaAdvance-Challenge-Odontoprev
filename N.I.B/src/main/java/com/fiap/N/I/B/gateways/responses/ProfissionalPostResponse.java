package com.fiap.N.I.B.gateways.responses;

import com.fiap.N.I.B.model.Profissional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalPostResponse {

    private String mensagem;
    private Profissional profissional;

}
