package com.fiap.N.I.B.usecases;

import com.fiap.N.I.B.domains.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Criar um novo usuário
    Usuario criarUsuario(Usuario usuario);

    // Buscar um usuário pelo CPF
    Optional<Usuario> buscarPorCpf(String cpf);

    // Buscar todos os usuários
    List<Usuario> buscarTodos();

    // Buscar usuários por plano
    List<Usuario> buscarPorPlano(String planoUser);

    // Buscar usuários por plano com paginação
    Page<Usuario> buscarPorPlanoPaginado(String planoUser, Pageable pageable);

    // Buscar usuários por data de nascimento com paginação
    Page<Usuario> buscarPorDataNascimentoPaginado(Date dataNascimentoUser, Pageable pageable);

    // Buscar usuários por data de nascimento em lista
    List<Usuario> buscarPorDataNascimentoEmLista(Date dataNascimentoUser);

    // Atualizar um usuário existente
    Optional<Usuario> atualizarUsuario(String cpf, Usuario usuarioAtualizado);

    // Deletar um usuário
    boolean deletarUsuario(String cpf);
}
