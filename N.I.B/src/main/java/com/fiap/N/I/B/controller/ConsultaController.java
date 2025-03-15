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
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(consulta.getUsuario().getCpfUser());

        Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(consulta.getProfissional().getRegistroProfissional());

        if (usuario.isPresent() && profissional.isPresent()) {
            Consulta consulta1 = Consulta.builder()
                    .id(consulta.getId())
                    .dataConsulta(consulta.getDataConsulta())
                    .descricaoConsulta(consulta.getDescricaoConsulta())
                    .usuario(usuario.get())
                    .profissional(profissional.get())
                    .build();

            if (consulta.getDataConsulta() != null && consulta.getDescricaoConsulta() != null) {
                consultaRepository.save(consulta1);
                Usuario usuarioAtualizado = usuario.get();
                usuarioAtualizado.getConsultas().add(consulta1);
                usuarioRepository.save(usuarioAtualizado);
                Profissional profissionalAtualizado = profissional.get();
                profissionalAtualizado.getConsultas().add(consulta1);
                profissionalRepository.save(profissionalAtualizado);
                return new ModelAndView("redirect:/consultas", "sucesso", "Consulta salva com sucesso!");
            } else {
                return new ModelAndView("Consultas/cadastrar-consulta", "erro", "Campos obrigatórios não preenchidos.");
            }
        } else {
            return new ModelAndView("Consultas/cadastrar-consulta", "erro", "Usuário ou Profissional não encontrados.");
        }
    }


    @GetMapping
    public ModelAndView listarConsultas() {
        List<Consulta> consultas = consultaRepository.findAll();
        return new ModelAndView("Consultas/lista", "consultas", consultas);
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarConsultaForm(@PathVariable Long id) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(String.valueOf(id));
        if (consultaOptional.isPresent()) {
            return new ModelAndView("Consultas/editar-consulta", "consulta", consultaOptional.get());
        }
        return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada.");
    }

    @PostMapping("/editar")
    public ModelAndView atualizarConsulta(@ModelAttribute Consulta consultaParam) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(String.valueOf(consultaParam.getId()));

        if (consultaOptional.isPresent()) {
            Consulta consultaTransacao = consultaOptional.get();
            Consulta consultaAtualizada = Consulta.builder()
                    .id(consultaParam.getId())
                    .dataConsulta(consultaParam.getDataConsulta())
                    .descricaoConsulta(consultaParam.getDescricaoConsulta())
                    .usuario(consultaTransacao.getUsuario())
                    .profissional(consultaTransacao.getProfissional())
                    .build();

            consultaRepository.save(consultaAtualizada);
            return new ModelAndView("redirect:/consultas", "sucesso", "Consulta atualizada com sucesso!");
        }

        return new ModelAndView("Consultas/editar-consulta", "erro", "Erro ao atualizar a consulta.");
    }

    @GetMapping("/deletar/{id}")
    public ModelAndView deletarConsulta(@PathVariable Long id) {
        Optional<Consulta> consultaOptional = consultaRepository.findById(String.valueOf(id));

        if (consultaOptional.isPresent()) {
            consultaRepository.deleteById(String.valueOf(id));
            return new ModelAndView("redirect:/consultas", "sucesso", "Consulta deletada com sucesso!");
        }

        return new ModelAndView("redirect:/consultas", "erro", "Consulta não encontrada para exclusão.");
    }
}