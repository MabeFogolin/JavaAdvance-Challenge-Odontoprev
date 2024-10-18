package com.fiap.N.I.B.usecases.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.UsuarioPatch;
import com.fiap.N.I.B.gateways.responses.UsuarioPostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Criar um novo usuário
    UsuarioPostResponse criarUsuario(Usuario usuario);

    // Buscar um usuário pelo CPF
    Optional<Usuario> buscarPorCpf(String cpf);

    // Buscar todos os usuários
    List<Usuario> buscarTodos();

    // Buscar usuários por plano
    List<Usuario> buscarPorPlano(String planoUser);

    // Buscar usuários por data de nascimento em lista
    List<Usuario> buscarPorDataNascimentoEmLista(LocalDate dataNascimentoUser);

    // Atualizar um usuário existente
    Optional<Usuario> atualizarUsuario(String cpf, Usuario usuarioAtualizado);

    //Atualizar email e plano do usuário
    Optional<Usuario> atualizarEmailPlano(String cpf, UsuarioPatch usuarioNovoEmailPlano);

    // Deletar um usuário
    boolean deletarUsuario(String cpf);
}
