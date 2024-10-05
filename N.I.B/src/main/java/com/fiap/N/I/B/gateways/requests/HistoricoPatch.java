package com.fiap.N.I.B.gateways.requests;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricoPatch {

    @Null
    private Integer tratamentoHistorico;

    @Null
    private Integer canalHistorico;

    @Null
    private Integer limpezaHistorico;

    @Null
    private Integer carieHistorico;

    @Null
    private Integer ortodonticoHistorico;

    @Null
    private Integer cirurgiaHistorico;
}
