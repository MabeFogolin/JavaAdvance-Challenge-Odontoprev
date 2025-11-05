package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.UsuarioPatch;
import com.fiap.N.I.B.gateways.responses.UsuarioPostResponse;
import com.fiap.N.I.B.usecases.Usuario.UsuarioService;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioPostResponse criarUsuario(Usuario usuarioEntrada) {
        Optional<Usuario> usuarioBusca = usuarioRepository.findByCpfUser(usuarioEntrada.getCpfUser());
        if (usuarioBusca.isEmpty()) {
            usuarioRepository.save(usuarioEntrada);
            return new UsuarioPostResponse("Novo usuário cadastrado", usuarioEntrada);
        } else {
            return new UsuarioPostResponse("CPF já cadastrado no sistema", usuarioBusca.get());
        }
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findByCpfUser(cpf);
    }

    @Override
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> buscarPorPlano(String planoUser) {
        return usuarioRepository.findUsuariosByPlanoUser(planoUser);
    }


    @Override
    public List<Usuario> buscarPorDataNascimentoEmLista(LocalDate dataNascimentoUser) {
        return usuarioRepository.findUsuariosByDataNascimentoUser(dataNascimentoUser);
    }

    @Transactional
    @Override
    public Optional<Usuario> atualizarUsuario(String cpf, Usuario usuarioAtualizado) {
        return usuarioRepository.findByCpfUser(cpf)
                .map(usuario -> {
                    usuario.setNomeUser(usuarioAtualizado.getNomeUser());
                    usuario.setSobrenomeUser(usuarioAtualizado.getSobrenomeUser());
                    usuario.setTelefoneUser(usuarioAtualizado.getTelefoneUser());
                    usuario.setDataNascimentoUser(usuarioAtualizado.getDataNascimentoUser());
                    usuario.setPlanoUser(usuarioAtualizado.getPlanoUser());
                    usuario.setEmailUser(usuarioAtualizado.getEmailUser());
                    return usuarioRepository.save(usuario);
                });
    }

    @Override
    public Optional<Usuario> atualizarEmailPlano(String cpf, UsuarioPatch usuarioNovoEmailPlano) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCpfUser(cpf);
        if (usuarioExistente.isPresent()) {
            Usuario usuarioNovo = usuarioExistente.get();
            usuarioNovo.setEmailUser(usuarioNovoEmailPlano.getEmailUser());
            usuarioNovo.setTelefoneUser(String.valueOf(usuarioNovoEmailPlano.getTelefoneUser()));
            Usuario usuarioAtualizado = usuarioRepository.save(usuarioNovo);
            return Optional.of(usuarioAtualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Usuario> atualizarPatch(String cpf, UsuarioPatch usuarioAtualizado) {
        return usuarioRepository.findByCpfUser(cpf)
                .map(usuarioExistente -> {
                    boolean houveAlteracao = false;

                    if (usuarioAtualizado.getNomeUser() != null
                            && !usuarioAtualizado.getNomeUser().equals(usuarioExistente.getNomeUser())) {
                        usuarioExistente.setNomeUser(usuarioAtualizado.getNomeUser());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getSobrenomeUser() != null
                            && !usuarioAtualizado.getSobrenomeUser().equals(usuarioExistente.getSobrenomeUser())) {
                        usuarioExistente.setSobrenomeUser(usuarioAtualizado.getSobrenomeUser());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getTelefoneUser() != null
                            && !usuarioAtualizado.getTelefoneUser().equals(usuarioExistente.getTelefoneUser())) {
                        usuarioExistente.setTelefoneUser(usuarioAtualizado.getTelefoneUser());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getEmailUser() != null
                            && !usuarioAtualizado.getEmailUser().equals(usuarioExistente.getEmailUser())) {
                        usuarioExistente.setEmailUser(usuarioAtualizado.getEmailUser());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getPontos() != null
                            && !usuarioAtualizado.getPontos().equals(usuarioExistente.getPontos())) {
                        usuarioExistente.setPontos(usuarioAtualizado.getPontos());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getNota() != null
                            && !usuarioAtualizado.getNota().equals(usuarioExistente.getNota())) {
                        usuarioExistente.setNota(usuarioAtualizado.getNota());
                        houveAlteracao = true;
                    }

                    if (usuarioAtualizado.getSequenciaDias() != null
                            && !usuarioAtualizado.getSequenciaDias().equals(usuarioExistente.getSequenciaDias())) {
                        usuarioExistente.setSequenciaDias(usuarioAtualizado.getSequenciaDias());
                        houveAlteracao = true;
                    }

                    if (houveAlteracao) {
                        return usuarioRepository.save(usuarioExistente);
                    }

                    return usuarioExistente; // retorna sem salvar, pois nada mudou
                });
    }


    @Override
    public boolean deletarUsuario(String cpf) {
        return usuarioRepository.findByCpfUser(cpf)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return true;
                }).orElse(false);
    }
}
