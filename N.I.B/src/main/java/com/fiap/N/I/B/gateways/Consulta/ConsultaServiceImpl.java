package com.fiap.N.I.B.gateways.Consulta;

import com.fiap.N.I.B.domains.Consulta;
import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.Repositories.ConsultaRepository;
import com.fiap.N.I.B.gateways.requests.ConsultaPatch;
import com.fiap.N.I.B.gateways.responses.ConsultaPostResponse;
import com.fiap.N.I.B.usecases.Consulta.ConsultaService;
import com.fiap.N.I.B.gateways.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultaServiceImpl implements ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;


    @Override
    public ConsultaPostResponse criarConsulta(String cpfUser, String registroProfissional, Consulta consultaParaInserir) {
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(cpfUser);
        Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional);

        if (usuario.isPresent() && profissional.isPresent()) {
            // Define o usuário e o profissional na consulta
            consultaParaInserir.setUsuario(usuario.get());
            consultaParaInserir.setProfissional(profissional.get());

            // Salva a consulta
            consultaRepository.save(consultaParaInserir);

            // Atualiza a lista de consultas do usuário
            Usuario usuarioAtualizado = usuario.get();
            usuarioAtualizado.getConsultas().add(consultaParaInserir);
            usuarioRepository.save(usuarioAtualizado);

            // Atualiza a lista de consultas do profissional
            Profissional profissionalAtualizado = profissional.get();
            profissionalAtualizado.getConsultas().add(consultaParaInserir);
            profissionalRepository.save(profissionalAtualizado);

            // Retorna resposta de sucesso
            return new ConsultaPostResponse("Nova consulta adicionada", consultaParaInserir);
        } else if (usuario.isEmpty()) {
            return new ConsultaPostResponse("Consulta não adicionada, usuário não encontrado", null);
        } else {
            return new ConsultaPostResponse("Consulta não adicionada, profissional não encontrado", null);
        }
    }


    @Override
    public List<Consulta> consultasPorUsuario(String cpfUser) {
        return consultaRepository.findByUsuario_CpfUser(cpfUser);
    }

    @Override
    public Optional<Consulta> atualizarConsultaTotalmente(String cpfUser, LocalDate dataConsulta, Consulta consultaParaAtualizar) {
        Optional<Consulta> consultaExistente = consultaRepository.findConsultaByUsuario_CpfUserAndDataConsulta(cpfUser, dataConsulta);

        if (consultaExistente.isPresent()) {
            Consulta consultaAtualizada = consultaExistente.map(c -> {
                c.setDataConsulta(consultaParaAtualizar.getDataConsulta());
                c.setDescricaoConsulta(consultaParaAtualizar.getDescricaoConsulta());
                c.setProfissional(consultaParaAtualizar.getProfissional());
                return consultaRepository.save(c);
            }).get();

            return Optional.of(consultaAtualizada);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deletarRegistro(String cpfUser, LocalDate dataConsulta) {
        return consultaRepository.findConsultaByUsuario_CpfUserAndDataConsulta(cpfUser, dataConsulta)
                .map(consulta -> {
                    consultaRepository.delete(consulta);
                    return true;
                }).orElse(false);
    }

    @Override
    public Optional<Consulta> atualizarInformacoesConsulta(String cpfUser, String registroProfissional, LocalDate dataConsulta, ConsultaPatch consultaPatch) {
        return consultaRepository.findConsultaByProfissional_RegistroProfissionalAndUsuario_CpfUserAndDataConsulta(cpfUser, registroProfissional, dataConsulta)
                .map(consultaExistente -> {
                    if (consultaPatch.getDataConsulta() != null) {
                        consultaExistente.setDataConsulta(consultaPatch.getDataConsulta());
                    }
                    if (consultaPatch.getDescricaoConsulta() != null) {
                        consultaExistente.setDescricaoConsulta(consultaPatch.getDescricaoConsulta());
                    }
                    return consultaRepository.save(consultaExistente);
                });
    }

    @Override
    public List<Consulta> todosRegistros() {
        return consultaRepository.findAll();
    }

    @Override
    public List<Consulta> consultasPorProfissional(String registroProfissional) {
        return consultaRepository.findConsultaByProfissional_RegistroProfissional(registroProfissional);
    }

    @Override
    public Optional<Consulta> buscarConsultaPorData(String cpfUser, LocalDate dataConsulta) {
        return consultaRepository.findConsultaByUsuario_CpfUserAndDataConsulta(cpfUser, dataConsulta);
    }
}
