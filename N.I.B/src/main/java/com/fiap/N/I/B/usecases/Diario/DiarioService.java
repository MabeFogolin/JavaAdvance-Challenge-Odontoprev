package com.fiap.N.I.B.usecases.Diario;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DiarioService {

    DiarioPostResponse inserirNoDiario(String cpfUser, Diario registroParaInserir);

    List<Diario> buscarRegistrosPorUsuario(String cpfUser);

    Optional<Diario> atualizarRegistro (String cpfUser, LocalDate dataRegistro, Diario registro);

    boolean deletarRegistro(String cpfUser, LocalDate dataRegistro);

    Optional<Diario> atualizarInformacoesRegistro(String cpfUser, LocalDate dataRegistro, DiarioPatch registroParaAtualizar);

    List<Diario> buscarTodos();

}
