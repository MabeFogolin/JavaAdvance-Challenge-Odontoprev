package com.fiap.N.I.B.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.N.I.B.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.EnderecoRepository;
import com.fiap.N.I.B.model.Profissional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/profissional")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalRepository profissionalRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final EnderecoRepository enderecoRepository;

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
        Profissional novoProfissional = Profissional.builder()
                .registroProfissional(profissionalParam.getRegistroProfissional())
                .nomeProfissional(profissionalParam.getNomeProfissional())
                .sobrenomeProfissional(profissionalParam.getSobrenomeProfissional())
                .telefoneProfissional(profissionalParam.getTelefoneProfissional())
                .emailProfissional(profissionalParam.getEmailProfissional())
                .tipoProfissional(profissionalParam.getTipoProfissional())
                .dataInscricaoProfissional(profissionalParam.getDataInscricaoProfissional())
                .consultas(profissionalParam.getConsultas())
                .build();

        profissionalRepository.save(novoProfissional);

        Endereco enderecoParam = profissionalParam.getEndereco();

        Endereco endereco = Endereco.builder()
                .ruaEndereco(enderecoParam.getRuaEndereco())
                .numeroEndereco(enderecoParam.getNumeroEndereco())
                .complementoEndereco(enderecoParam.getComplementoEndereco())
                .bairroEndereco(enderecoParam.getBairroEndereco())
                .cidadeEndereco(enderecoParam.getCidadeEndereco())
                .cepEndereco(enderecoParam.getCepEndereco())
                .estadoEndereco(enderecoParam.getEstadoEndereco())
                .profissional(novoProfissional)
                .build();

        endereco = enderecoRepository.save(endereco);

        novoProfissional.setEndereco(endereco);
        profissionalRepository.save(novoProfissional);

        return new ModelAndView("redirect:/profissional", "sucesso", "Profissional cadastrado com sucesso!");
    }

    @GetMapping("/{registroProfissional}")
    public ModelAndView listarProfissional(@PathVariable String registroProfissional) {
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
            System.out.println(profissionalOptional.get().getNomeProfissional());
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

            try {
                String profissionalJson = objectMapper.writeValueAsString(profissionalAtualizado);
                rabbitTemplate.convertAndSend("profissionalExchange", "routingKey", profissionalJson);
                System.out.println("📩 Mensagem enviada para a fila: " + profissionalJson);
            } catch (JsonProcessingException e) {
                System.err.println("❌ Erro ao serializar o objeto Profissional: " + e.getMessage());
            }

            return new ModelAndView("redirect:/profissional", "sucesso", "Profissional atualizado com sucesso!");
        }

        return new ModelAndView("Profissional/editar-profissional", "erro", "Erro ao atualizar o profissional.");
    }

    @GetMapping("/deletar/{registro}")
    public ModelAndView deletarProfissional(@PathVariable String registro) {
        Optional<Profissional> profissionalOptional = profissionalRepository.findById(registro);
        if (profissionalOptional.isPresent()) {
            profissionalRepository.deletarProfissionalProcedure(registro);
            return new ModelAndView("redirect:/profissional", "sucesso", "Profissional deletado com sucesso!");
        }
        return new ModelAndView("redirect:/profissional", "erro", "Profissional não encontrado para exclusão.");
    }
}
