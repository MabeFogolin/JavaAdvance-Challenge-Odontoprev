package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.usecases.UsuarioService;
import com.fiap.N.I.B.usecases.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findUsuarioByCpfUser(cpf);
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
    public Page<Usuario> buscarPorPlanoPaginado(String planoUser, Pageable pageable) {
        return usuarioRepository.findUsuariosByPlanoUser(planoUser, pageable);
    }

    @Override
    public Page<Usuario> buscarPorDataNascimentoPaginado(Date dataNascimentoUser, Pageable pageable) {
        return usuarioRepository.findUsuariosByDataNascimentoUser(dataNascimentoUser, pageable);
    }

    @Override
    public List<Usuario> buscarPorDataNascimentoEmLista(Date dataNascimentoUser) {
        return usuarioRepository.findUsuariosByDataNascimentoUser(dataNascimentoUser);
    }

    @Override
    public Optional<Usuario> atualizarUsuario(String cpf, Usuario usuarioAtualizado) {
        return usuarioRepository.findUsuarioByCpfUser(cpf)
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
    public boolean deletarUsuario(String cpf) {
        return usuarioRepository.findUsuarioByCpfUser(cpf)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return true;
                }).orElse(false);
    }
}
