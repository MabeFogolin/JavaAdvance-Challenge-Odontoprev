package com.fiap.N.I.B.usecases.Historico;

import com.fiap.N.I.B.domains.Historico;
import com.fiap.N.I.B.gateways.requests.HistoricoPatch;
import com.fiap.N.I.B.gateways.responses.HistoricoPostResponse;

import java.util.List;
import java.util.Optional;

public interface HistoricoService {

    HistoricoPostResponse inserirNoHistorico(String cpfUser, Historico historico);

    Optional<Historico> buscarHistoricoPorUsuario(String cpfUser);

    Optional<Historico> atualizarHistoricoCompleto(String cpfUser, Historico historico);

    List<Historico> listarTodos();

    boolean deletarHistorico(String cpfUser);

    Optional<Historico> atualizarInformacoesHistorico(String cpfUser, HistoricoPatch historico);


}
