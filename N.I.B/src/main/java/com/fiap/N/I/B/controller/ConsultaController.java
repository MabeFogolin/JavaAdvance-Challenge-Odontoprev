package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.gateways.Repositories.ConsultaRepository;
import com.fiap.N.I.B.gateways.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import com.fiap.N.I.B.model.Consulta;

import com.fiap.N.I.B.model.Profissional;
import com.fiap.N.I.B.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;

    @GetMapping("/nova")
    public ModelAndView novaConsultaForm() {
        Consulta consultaVazia = new Consulta();
        return new ModelAndView("Consultas/cadastrar-consulta", "consulta", consultaVazia);
    }

    @PostMapping("/nova")
    public ModelAndView novaConsulta(@ModelAttribute Consulta consulta) {
        // Busca o usuário com o CPF fornecido
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(consulta.getUsuario().getCpfUser());

        Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(consulta.getProfissional().getRegistroProfissional());

        if (usuario.isPresent() && profissional.isPresent()) {
            // Construa a nova consulta com os dados fornecidos
            Consulta consulta1 = Consulta.builder()
                    .id(consulta.getId())
                    .dataConsulta(consulta.getDataConsulta())
                    .descricaoConsulta(consulta.getDescricaoConsulta())
                    .usuario(usuario.get())  // Atribui o usuário encontrado
                    .profissional(profissional.get())  // Atribui o profissional encontrado
                    .build();

            // Verifica se os campos obrigatórios foram preenchidos
            if (consulta.getDataConsulta() != null && consulta.getDescricaoConsulta() != null) {
                // Salva a nova consulta no banco de dados
                consultaRepository.save(consulta1);

                // Redireciona para a lista de consultas com uma mensagem de sucesso
                return new ModelAndView("redirect:/consultas", "sucesso", "Consulta salva com sucesso!");
            } else {
                // Caso haja erro de validação, retorna para a página de cadastro
                return new ModelAndView("Consultas/cadastrar-consulta", "erro", "Campos obrigatórios não preenchidos.");
            }
        } else {
            // Caso o usuário ou o profissional não existam, retorna erro
            return new ModelAndView("Consultas/cadastrar-consulta", "erro", "Usuário ou Profissional não encontrados.");
        }
    }


    @GetMapping
    public ModelAndView listarConsultas() {
        List<Consulta> consultas = consultaRepository.findAll(); // Recupera todas as consultas
        return new ModelAndView("Consultas/lista", "consultas", consultas); // Passa as consultas para a view
    }

    @GetMapping("/{id}")
    public ModelAndView detalhesConsulta(@PathVariable Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(String.valueOf(id)); // Busca a consulta pelo ID

        if (consultaOpt.isPresent()) {
            return new ModelAndView("consulta/detalhes", "consulta", consultaOpt.get()); // Passa a consulta para a view
        } else {
            return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada.");
        }
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarConsultaForm(@PathVariable Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(String.valueOf(id));

        if (consultaOpt.isPresent()) {
            return new ModelAndView("consulta/editar-consulta", "consulta", consultaOpt.get()); // Passa a consulta para o formulário de edição
        } else {
            return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada para edição.");
        }
    }

    @PutMapping("/editar/{id}")
    public ModelAndView atualizarConsulta(@PathVariable Long id, @ModelAttribute Consulta consulta) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(String.valueOf(id));

        if (consultaOpt.isPresent()) {
            consulta.setId(id); // Atribui o ID da consulta para garantir que estamos atualizando
            consultaRepository.save(consulta); // Atualiza a consulta no banco de dados
            return new ModelAndView("redirect:/consultas", "sucesso", "Consulta atualizada com sucesso!");
        } else {
            return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada para atualização.");
        }
    }


    @GetMapping("/deletar/{id}")
    public ModelAndView deletarConsulta(@PathVariable Long id) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(String.valueOf(id));

        if (consultaOpt.isPresent()) {
            consultaRepository.deleteById(String.valueOf(id));
            return new ModelAndView("redirect:/consultas", "sucesso", "Consulta deletada com sucesso!");
        } else {
            return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada para deletar.");
        }
    }
}