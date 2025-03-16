package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.gateways.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.model.Diario;
import com.fiap.N.I.B.model.Profissional;
import com.fiap.N.I.B.gateways.requests.ProfissionalPatch;
import com.fiap.N.I.B.gateways.responses.ProfissionalPostResponse;
import com.fiap.N.I.B.model.Usuario;
import com.fiap.N.I.B.usecases.Profissional.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/profissional")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalRepository profissionalRepository;

    @GetMapping
    public ModelAndView listarProfissionais() {
        List<Profissional> profissionais = profissionalRepository.findAll();
        return new ModelAndView("Profissional/lista", "profissionais", profissionais);
    }

    @GetMapping("/novo")
    public ModelAndView novoProfissionalForm() {
        Profissional profissionalVazio = new Profissional();
        return new ModelAndView("Profissional/cadastrar-profissional", "profissional", profissionalVazio);
    }

    @PostMapping("/novo")
    public ModelAndView novoProfissional(@ModelAttribute Profissional profissionalParam) {
        Optional<Profissional> profissionalExistente = profissionalRepository.findById(profissionalParam.getRegistroProfissional());

        if (profissionalExistente.isPresent()) {
            return new ModelAndView("Profissional/cadastrar-profissional", "erro", "Registro já cadastrado.");
        }

        // Validações dos campos
        if (profissionalParam.getNomeProfissional().length() > 20) {
            return new ModelAndView("Profissional/cadastrar-profissional", "erro", "O nome do profissional deve ter no máximo 20 caracteres.");
        }

        if (profissionalParam.getSobrenomeProfissional().length() > 30) {
            return new ModelAndView("Profissional/cadastrar-profissional", "erro", "O sobrenome do profissional deve ter no máximo 30 caracteres.");
        }

        if (!profissionalParam.getTelefoneProfissional().matches("\\d{11}")) {
            return new ModelAndView("Profissional/cadastrar-profissional", "erro", "O telefone deve conter exatamente 11 dígitos numéricos.");
        }

        Profissional novoProfissional = Profissional.builder()
                .registroProfissional(profissionalParam.getRegistroProfissional())
                .nomeProfissional(profissionalParam.getNomeProfissional())
                .sobrenomeProfissional(profissionalParam.getSobrenomeProfissional())
                .telefoneProfissional(profissionalParam.getTelefoneProfissional())
                .emailProfissional(profissionalParam.getEmailProfissional())
                .tipoProfissional(profissionalParam.getTipoProfissional())
                .dataInscricaoProfissional(profissionalParam.getDataInscricaoProfissional())
                .endereco(profissionalParam.getEndereco())
                .consultas(profissionalParam.getConsultas())
                .build();
        profissionalRepository.save(novoProfissional);

        return new ModelAndView("redirect:/profissional", "sucesso", "Profissional cadastrado com sucesso!");
    }

    @GetMapping("/{registroProfissional}")
    public ModelAndView listarDiario(@PathVariable String registroProfissional) {
        Optional<Profissional> profissionalOptional = profissionalRepository.findById(registroProfissional);
        if (profissionalOptional.isPresent()) {
            return new ModelAndView("Profissional/listar-profissional", "profissional", profissionalOptional.get());
        }
        return new ModelAndView("redirect:/profissional", "erro", "Profissional não encontrado.");
    }

    @GetMapping("/editar/{registroProfissional}")
    public ModelAndView editarProfissionalForm(@PathVariable String registroProfissional) {
        Optional<Profissional> profissionalOptional = profissionalRepository.findById(registroProfissional);
        if (profissionalOptional.isPresent()) {
            return new ModelAndView("Profissional/editar-profissional", "profissional", profissionalOptional.get());
        }
        return new ModelAndView("redirect:/profissional", "erro", "Profissional não encontrado.");
    }

    @PostMapping("/editar")
    public ModelAndView atualizarProfissional(@ModelAttribute Profissional profissionalParam) {
        Optional<Profissional> profissionalOptional = profissionalRepository.findById(profissionalParam.getRegistroProfissional());

        if (profissionalOptional.isPresent()) {
            Profissional profissionalAtualizado = Profissional.builder()
                    .registroProfissional(profissionalParam.getRegistroProfissional())
                    .nomeProfissional(profissionalParam.getNomeProfissional())
                    .sobrenomeProfissional(profissionalParam.getSobrenomeProfissional())
                    .telefoneProfissional(profissionalParam.getTelefoneProfissional())
                    .emailProfissional(profissionalParam.getEmailProfissional())
                    .tipoProfissional(profissionalParam.getTipoProfissional())
                    .dataInscricaoProfissional(profissionalParam.getDataInscricaoProfissional())
                    .endereco(profissionalParam.getEndereco())
                    .consultas(profissionalParam.getConsultas())
                    .build();

            profissionalRepository.save(profissionalAtualizado);
            return new ModelAndView("redirect:/profissional", "sucesso", "Profissional atualizado com sucesso!");
        }

        return new ModelAndView("Profissional/editar-profissional", "erro", "Erro ao atualizar o profissional.");
    }

    @GetMapping("/deletar/{registro}")
    public ModelAndView deletarProfissional(@PathVariable String registro) {
        Optional<Profissional> profissionalOptional = profissionalRepository.findById(registro);
        if (profissionalOptional.isPresent()) {
            profissionalRepository.deleteById(registro);
            return new ModelAndView("redirect:/profissional", "sucesso", "Profissional deletado com sucesso!");
        }
        return new ModelAndView("redirect:/profissional", "erro", "Profissional não encontrado para exclusão.");
    }
}
