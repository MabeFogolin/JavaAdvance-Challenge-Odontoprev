package com.fiap.N.I.B.gateways.Diario;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;
import com.fiap.N.I.B.gateways.Repositories.DiarioRepository;
import com.fiap.N.I.B.usecases.Diario.DiarioService;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiarioServiceImpl implements DiarioService {

    private final DiarioRepository diarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public DiarioPostResponse inserirNoDiario(String cpfUser, Diario registroParaInserir) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCpfUser(cpfUser);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            List<Diario> registrosAnteriores = diarioRepository.findByUsuario_CpfUser(cpfUser);
            registrosAnteriores.sort((a, b) -> b.getDataRegistro().compareTo(a.getDataRegistro()));
            Diario ultimoRegistro = registrosAnteriores.isEmpty() ? null : registrosAnteriores.get(0);


            boolean houveHigiene = registroParaInserir.getEscovacaoDiario() > 0;

            if (houveHigiene && ultimoRegistro != null) {
                long diferencaDias = java.time.temporal.ChronoUnit.DAYS.between(ultimoRegistro.getDataRegistro(), registroParaInserir.getDataRegistro());

                if (diferencaDias == 1 ) {
                    usuario.setSequenciaDias(usuario.getSequenciaDias() + 1);
                } else if (diferencaDias > 1) {
                    usuario.setSequenciaDias(1);
                }
                {
                    usuario.setSequenciaDias(usuario.getSequenciaDias());
                }
            }

            usuario.setPontos(usuario.getPontos() + 1);

            usuarioRepository.save(usuario);
            registroParaInserir.setUsuario(usuario);
            diarioRepository.save(registroParaInserir);

            return new DiarioPostResponse("Novo registro adicionado ao diário e pontos incrementados", registroParaInserir);
        } else {
            return new DiarioPostResponse("Registro não adicionado, não foi encontrado o usuário para atribuição", null);
        }
    }



    @Override
    public List<Diario> buscarRegistrosPorUsuario(String cpfUser) {
        return diarioRepository.findByUsuario_CpfUser(cpfUser);
    }

    @Override
    public List<Diario> buscarTodos() {
        return diarioRepository.findAll();
    }

    @Override
    public Optional<Diario> atualizarRegistro(String cpfUser, LocalDate dataRegistro, Diario registroParaAtualizar) {
        Optional<Diario> retornoRegistro = diarioRepository.findRegistroByUsuario_CpfUserAndDataRegistro(cpfUser, dataRegistro);

        if (retornoRegistro.isPresent()) {
            Diario registroAtualizado = retornoRegistro.map(registro -> {
                registro.setEscovacaoDiario(registroParaAtualizar.getEscovacaoDiario());
                registro.setUsoFioDiario(registroParaAtualizar.getUsoFioDiario());
                registro.setUsoEnxaguanteDiario(registroParaAtualizar.getUsoEnxaguanteDiario());
                registro.setSintomaDiario(registroParaAtualizar.getSintomaDiario());
                return diarioRepository.save(registro);
            }).get();

            return Optional.of(registroAtualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deletarRegistro(String cpfUser, LocalDate dataRegistro) {
        return diarioRepository.findRegistroByUsuario_CpfUserAndDataRegistro(cpfUser, dataRegistro)
                .map(registro -> {
                    diarioRepository.delete(registro);
                    return true;
                }).orElse(false);
    }

    @Override
    public Optional<Diario> atualizarInformacoesRegistro(String cpfUser, LocalDate dataRegistro, DiarioPatch registroParaAtualizar) {
        return diarioRepository.findRegistroByUsuario_CpfUserAndDataRegistro(cpfUser, dataRegistro)
                .map(registroExistente -> {

                    if (registroParaAtualizar.getEscovacaoDiario() != null) {
                        registroExistente.setEscovacaoDiario(registroParaAtualizar.getEscovacaoDiario());
                    }
                    if (registroParaAtualizar.getUsoFioDiario() != null) {
                        registroExistente.setUsoFioDiario(registroParaAtualizar.getUsoFioDiario());
                    }
                    if (registroParaAtualizar.getUsoEnxaguanteDiario() != null) {
                        registroExistente.setUsoEnxaguanteDiario(registroParaAtualizar.getUsoEnxaguanteDiario());
                    }
                    if (registroParaAtualizar.getSintomaDiario() != null) {
                        registroExistente.setSintomaDiario(registroParaAtualizar.getSintomaDiario());
                    }
                    return diarioRepository.save(registroExistente);
                });
    }

}

