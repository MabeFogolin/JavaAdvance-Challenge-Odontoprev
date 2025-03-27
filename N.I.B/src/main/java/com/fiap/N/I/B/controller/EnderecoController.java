package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.EnderecoRepository;
import com.fiap.N.I.B.ignore.gateways.Endereco.EnderecoServiceImpl;
import com.fiap.N.I.B.ignore.gateways.responses.EnderecoPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoServiceImpl enderecoService;
    private final EnderecoRepository enderecoRepository;

    @GetMapping("/novo")
    public ModelAndView novoEnderecoForm() {
        Endereco enderecoVazio = new Endereco();
        return new ModelAndView("Enderecos/cadastrar-endereco", "endereco", enderecoVazio);
    }

    @PostMapping("/novo")
    public ModelAndView criarEndereco(@ModelAttribute Endereco endereco) {
        String idPessoa = endereco.getUsuario() != null ? endereco.getUsuario().getCpfUser() : endereco.getProfissional().getRegistroProfissional();

        EnderecoPostResponse response = enderecoService.criarEndereco(idPessoa, endereco);

        if (response.getEndereco() != null) {
            return new ModelAndView("redirect:/enderecos", "sucesso", response.getMensagem());
        } else {
            return new ModelAndView("Enderecos/cadastrar-endereco", "erro", response.getMensagem());
        }
    }

    @GetMapping
    public ModelAndView listarTodosEnderecos() {
        List<Endereco> enderecos = enderecoService.listarTodos();
        return new ModelAndView("Enderecos/lista", "enderecos", enderecos);
    }

    @GetMapping("/{id}")
    public ModelAndView buscarEnderecoPorId(@PathVariable Long id) {
        Optional<Endereco> enderecoOptional = enderecoService.listarTodos().stream().filter(e -> e.getId().equals(id)).findFirst();

        if (enderecoOptional.isPresent()) {
            return new ModelAndView("Enderecos/lista-endereco", "endereco", enderecoOptional.get());
        } else {
            return new ModelAndView("redirect:/enderecos", "erro", "Endereço não encontrado.");
        }
    }



    @GetMapping("/editar/{id}")
    public ModelAndView editarConsultaForm(@PathVariable Long id) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(String.valueOf(id));
        if (enderecoOptional.isPresent()) {
            return new ModelAndView("Enderecos/editar-endereco", "endereco", enderecoOptional.get());
        }
        return new ModelAndView("redirect:/enderecos", "erro", "Consulta não encontrada.");
    }

    @PostMapping("/editar")
    public ModelAndView atualizarEndereco(@ModelAttribute Endereco enderecoParam) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(String.valueOf(enderecoParam.getId()));

        if (enderecoOptional.isPresent()) {
            Endereco enderecoTransacao = enderecoOptional.get();

            Endereco enderecoAtualizado = Endereco.builder()
                    .id(enderecoParam.getId())
                    .ruaEndereco(enderecoParam.getRuaEndereco() != null ? enderecoParam.getRuaEndereco() : enderecoTransacao.getRuaEndereco())
                    .numeroEndereco(String.valueOf(enderecoParam.getNumeroEndereco()) != null ? enderecoParam.getNumeroEndereco() : enderecoTransacao.getNumeroEndereco())
                    .complementoEndereco(enderecoParam.getComplementoEndereco() != null ? enderecoParam.getComplementoEndereco() : enderecoTransacao.getComplementoEndereco())
                    .bairroEndereco(enderecoParam.getBairroEndereco() != null ? enderecoParam.getBairroEndereco() : enderecoTransacao.getBairroEndereco())
                    .cidadeEndereco(enderecoParam.getCidadeEndereco() != null ? enderecoParam.getCidadeEndereco() : enderecoTransacao.getCidadeEndereco())
                    .cepEndereco(enderecoParam.getCepEndereco() != null ? enderecoParam.getCepEndereco() : enderecoTransacao.getCepEndereco())
                    .estadoEndereco(enderecoParam.getEstadoEndereco() != null ? enderecoParam.getEstadoEndereco() : enderecoTransacao.getEstadoEndereco())
                    .usuario(enderecoTransacao.getUsuario()) // Mantém a referência ao usuário existente, se aplicável
                    .profissional(enderecoTransacao.getProfissional()) // Mantém a referência ao profissional existente, se aplicável
                    .build();

            enderecoRepository.save(enderecoAtualizado);
            return new ModelAndView("redirect:/enderecos", "sucesso", "Endereço atualizado com sucesso!");
        }

        return new ModelAndView("Enderecos/editar-endereco", "erro", "Erro ao atualizar o endereço.");
    }


    @GetMapping("/deletar/{id}")
    public ModelAndView deletarConsulta(@PathVariable String id) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(String.valueOf(id));

        if (enderecoOptional.isPresent()) {
            enderecoRepository.deleteById(id);
            Optional<Endereco> enderecoExclusao = enderecoRepository.findById(id);
            if (enderecoExclusao.isEmpty()) {
                return new ModelAndView("redirect:/enderecos", "sucesso", "Endereço deletado com sucesso!");
            }

        }
        return new ModelAndView("redirect:/enderecos", "erro", "Endereço não encontrado para exclusão.");
    }
}

