package com.fiap.N.I.B.gateways.Historico;

import com.fiap.N.I.B.domains.Historico;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.HistoricoPatch;
import com.fiap.N.I.B.gateways.responses.HistoricoPostResponse;
import com.fiap.N.I.B.gateways.Repositories.HistoricoRepository;
import com.fiap.N.I.B.usecases.Historico.HistoricoService;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoServiceImpl implements HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public HistoricoPostResponse inserirNoHistorico(String cpfUser, Historico historico) {
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(cpfUser);

        if (usuario.isPresent()) {
            historico.setUsuario(usuario.get());
            historicoRepository.save(historico);
            return new HistoricoPostResponse("Novo registro adicionado ao histórico", historico);
        } else {
            return new HistoricoPostResponse("Registro não adicionado, usuário não encontrado", null);
        }
    }

    @Override
    public Optional<Historico> buscarHistoricoPorUsuario(String cpfUser) {
        return historicoRepository.findByUsuario_CpfUser(cpfUser);
    }

    @Override
    public Optional<Historico> atualizarHistoricoCompleto(String cpfUser, Historico historicoParaAtualizar) {
        Optional<Historico> historicoExistente = historicoRepository.findByUsuario_CpfUser(cpfUser);

        if (historicoExistente.isPresent()) {
            Historico historicoAtualizado = historicoExistente.map(h -> {
                h.setTratamentoHistorico(historicoParaAtualizar.getTratamentoHistorico());
                h.setCanalHistorico(historicoParaAtualizar.getCanalHistorico());
                h.setLimpezaHistorico(historicoParaAtualizar.getLimpezaHistorico());
                h.setCarieHistorico(historicoParaAtualizar.getCarieHistorico());
                h.setOrtodonticoHistorico(historicoParaAtualizar.getOrtodonticoHistorico());
                h.setCirurgiaHistorico(historicoParaAtualizar.getCirurgiaHistorico());
                return historicoRepository.save(h);
            }).get();

            return Optional.of(historicoAtualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Historico> listarTodos() {
        return historicoRepository.findAll();
    }

    @Override
    public boolean deletarHistorico(String cpfUser) {
        return historicoRepository.findByUsuario_CpfUser(cpfUser)
                .map(historico -> {
                    historicoRepository.delete(historico);
                    return true;
                }).orElse(false);
    }

    @Override
    public Optional<Historico> atualizarInformacoesHistorico(String cpfUser, HistoricoPatch historicoPatch) {
        return historicoRepository.findByUsuario_CpfUser(cpfUser)
                .map(historicoExistente -> {
                    if (historicoPatch.getTratamentoHistorico() != null) {
                        historicoExistente.setTratamentoHistorico(historicoPatch.getTratamentoHistorico());
                    }
                    if (historicoPatch.getCanalHistorico() != null) {
                        historicoExistente.setCanalHistorico(historicoPatch.getCanalHistorico());
                    }
                    if (historicoPatch.getLimpezaHistorico() != null) {
                        historicoExistente.setLimpezaHistorico(historicoPatch.getLimpezaHistorico());
                    }
                    if (historicoPatch.getCarieHistorico() != null) {
                        historicoExistente.setCarieHistorico(historicoPatch.getCarieHistorico());
                    }
                    if (historicoPatch.getOrtodonticoHistorico() != null) {
                        historicoExistente.setOrtodonticoHistorico(historicoPatch.getOrtodonticoHistorico());
                    }
                    if (historicoPatch.getCirurgiaHistorico() != null) {
                        historicoExistente.setCirurgiaHistorico(historicoPatch.getCirurgiaHistorico());
                    }
                    return historicoRepository.save(historicoExistente);
                });
    }

}