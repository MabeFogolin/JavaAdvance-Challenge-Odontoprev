package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.UsuarioPatch;
import com.fiap.N.I.B.gateways.responses.UsuarioPostResponse;
import com.fiap.N.I.B.usecases.Usuario.UsuarioService;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
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
            usuarioNovo.setPlanoUser(usuarioNovoEmailPlano.getPlanoUser());
            Usuario usuarioAtualizado = usuarioRepository.save(usuarioNovo);
            return Optional.of(usuarioAtualizado);
        } else {
            return Optional.empty();
        }
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
