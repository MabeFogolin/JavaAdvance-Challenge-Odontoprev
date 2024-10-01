package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Consulta;
import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.ConsultaPatch;
import com.fiap.N.I.B.gateways.responses.ConsultaPostResponse;
import com.fiap.N.I.B.usecases.ConsultaRepository;
import com.fiap.N.I.B.usecases.ConsultaService;
import com.fiap.N.I.B.usecases.ProfissionalRepository;
import com.fiap.N.I.B.usecases.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
            Optional<Usuario> usuario = usuarioRepository.findUsuarioByCpfUser(cpfUser);
            Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional);

            if (usuario.isPresent() || profissional.isPresent()) {
                consultaParaInserir.setUsuario(usuario.get());
                consultaParaInserir.setProfissional(profissional.get());
                consultaRepository.save(consultaParaInserir);
                return new ConsultaPostResponse("Nova consulta adicionada", consultaParaInserir);
            } else if(usuario.isEmpty()){
                return new ConsultaPostResponse("Consulta não adicionada, usuário não encontrado", null);
            } else{
                return new ConsultaPostResponse("Consulta não adicionada, profissional não encontrado", null);
            }
        }

    @Override
    public List<Consulta> consultasPorUsuario(String cpfUser) {
        return consultaRepository.findByUsuario_CpfUser(cpfUser);
    }

    @Override
    public Optional<Consulta> atualizarConsultaTotalmente(String cpfUser, Date dataConsulta, Consulta consultaParaAtualizar) {
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
    public boolean deletarRegistro(String cpfUser, Date dataConsulta) {
        return consultaRepository.findConsultaByUsuario_CpfUserAndDataConsulta(cpfUser, dataConsulta)
                .map(consulta -> {
                    consultaRepository.delete(consulta);
                    return true;
                }).orElse(false);
    }

    @Override
    public Optional<Consulta> atualizarInformacoesConsulta(String cpfUser, String registroProfissional, Date dataConsulta, ConsultaPatch consultaPatch) {
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
}
