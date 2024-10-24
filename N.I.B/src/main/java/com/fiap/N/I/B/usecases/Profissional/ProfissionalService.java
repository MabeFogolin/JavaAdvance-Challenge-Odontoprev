package com.fiap.N.I.B.usecases.Profissional;

import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.gateways.requests.ProfissionalPatch;
import com.fiap.N.I.B.gateways.responses.ProfissionalPostResponse;

import java.util.List;
import java.util.Optional;

public interface ProfissionalService {

    //Cria um novo objeto do tipo Profissional
    ProfissionalPostResponse criarProfissional(Profissional profissionalParaCriar);

    //Busca Profissional pelo registro
    Optional<Profissional> buscarProfissional(String registroProfissional);

    //Retorna todos os objetos do tipo Profissional cadastrados
    List<Profissional> buscarTodos();

    //Retorna objetos do tipo Profissional com base no tipo cadastrado (Categoria do profissional)
    List<Profissional> buscarPorCategoria(String tipoProfissional);

    //Retorna o objeto Profissional atualizado totalmente
    Optional<Profissional> atualizarProfissional(String registroProfissional, Profissional profissionalParaAtualizar);

    boolean deletarProfissional(String registroProfissional);

    Optional<Profissional> atualizarEmailTelefone(String registroProfissional, ProfissionalPatch profissionalPatch);

//    boolean deletarProfissionalProcedure(String registroProfissional);
}
