package com.fiap.N.I.B.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.N.I.B.Repositories.UsuarioRepository;
import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.EnderecoRepository;
import com.fiap.N.I.B.model.Usuario;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
@EnableWebSecurity
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final EnderecoRepository enderecoRepository;



    @GetMapping("/novo")
    public ModelAndView novoUsuarioForm() {
        Usuario usuarioVazio = new Usuario();
        return new ModelAndView("Usuario/cadastrar-usuario", "usuario", usuarioVazio);
    }

    @PostMapping("/novo")
    public ModelAndView novoUsuario(@ModelAttribute Usuario usuarioParam) {
        // Verifica se o CPF já existe
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCpfUser(usuarioParam.getCpfUser());

        if (usuarioExistente.isPresent()) {
            return new ModelAndView("Usuario/cadastrar-usuario", "erro", "CPF já cadastrado.");
        }

        Usuario newUsuario = Usuario.builder()
                .cpfUser(usuarioParam.getCpfUser())
                .planoUser(usuarioParam.getPlanoUser())
                .dataNascimentoUser(usuarioParam.getDataNascimentoUser())
                .emailUser(usuarioParam.getEmailUser())
                .nomeUser(usuarioParam.getNomeUser())
                .sobrenomeUser(usuarioParam.getSobrenomeUser())
                .telefoneUser(usuarioParam.getTelefoneUser())
                .diarios(usuarioParam.getDiarios())
                .build();

        newUsuario = usuarioRepository.save(newUsuario);
        Endereco enderecoParam = usuarioParam.getEndereco();

        Endereco endereco = Endereco.builder()
                .ruaEndereco(enderecoParam.getRuaEndereco())
                .numeroEndereco(enderecoParam.getNumeroEndereco())
                .complementoEndereco(enderecoParam.getComplementoEndereco())
                .bairroEndereco(enderecoParam.getBairroEndereco())
                .cidadeEndereco(enderecoParam.getCidadeEndereco())
                .cepEndereco(enderecoParam.getCepEndereco())
                .estadoEndereco(enderecoParam.getEstadoEndereco())
                .usuario(newUsuario)
                .build();

        endereco = enderecoRepository.save(endereco);

        newUsuario.setEndereco(endereco);
        usuarioRepository.save(newUsuario);

        return new ModelAndView("redirect:/usuario", "sucesso", "Usuário cadastrado com sucesso!");
    }


    @Counted(value = "usuario.count.listarUsuarios")
    @Timed(value = "usuario.timed.listarUsuarios", longTask = true)
    @GetMapping
    public ModelAndView listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return new ModelAndView("Usuario/lista", "usuarios", usuarios);
    }

    @GetMapping("/editar/{cpf}")
    public ModelAndView editarUsuarioForm(@PathVariable String cpf, @AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cpf);
        if (usuarioOptional.isPresent()) {
            return new ModelAndView("Usuario/editar-usuario", "usuario", usuarioOptional.get());
        }
        return new ModelAndView("redirect:/usuario", "erro", "Usuário não encontrado.");
    }


    @GetMapping("/{cpf}")
    public ModelAndView listarUsuario(@PathVariable String cpf, @AuthenticationPrincipal UserDetails user) {
        if(user != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(cpf);
            if (usuarioOptional.isPresent()) {
                return new ModelAndView("Usuario/listar-usuario", "usuario", usuarioOptional.get());
            }
            return new ModelAndView("redirect:/usuario", "erro", "Usuário não encontrado.");

        }
        return new ModelAndView("redirect:/usuario", "erro", "Usuário não autenticado");

    }

    @PostMapping("/editar")
    public ModelAndView atualizarUsuario(@ModelAttribute Usuario usuarioParam) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioParam.getCpfUser());

        if (usuarioOptional.isPresent()) {
            Usuario usuarioAtualizado = Usuario.builder()
                    .cpfUser(usuarioParam.getCpfUser())
                    .planoUser(usuarioParam.getPlanoUser())
                    .dataNascimentoUser(usuarioParam.getDataNascimentoUser())
                    .emailUser(usuarioParam.getEmailUser())
                    .nomeUser(usuarioParam.getNomeUser())
                    .sobrenomeUser(usuarioParam.getSobrenomeUser())
                    .telefoneUser(usuarioParam.getTelefoneUser())
                    .endereco(usuarioParam.getEndereco())
                    .diarios(usuarioParam.getDiarios())
                    .build();

            try {
                String usuarioJson = objectMapper.writeValueAsString(usuarioAtualizado);
                rabbitTemplate.convertAndSend("usuarioExchange", "routingKey", usuarioJson);
                System.out.println("📩 Mensagem enviada para a fila: " + usuarioJson);
            } catch (JsonProcessingException e) {
                System.err.println("❌ Erro ao serializar o objeto Usuario: " + e.getMessage());
            }
            return new ModelAndView("redirect:/usuario", "sucesso", "Usuário atualizado com sucesso!");
        }

        return new ModelAndView("Usuario/editar-usuario", "erro", "Erro ao atualizar o usuário.");
    }

    @GetMapping("/deletar/{cpf}")
    public ModelAndView deletarUsuario(@PathVariable String cpf) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cpf);
        if (usuarioOptional.isPresent()) {
            usuarioRepository.deleteById(cpf);
            return new ModelAndView("redirect:/usuario", "sucesso", "Usuário deletado com sucesso!");
        }
        return new ModelAndView("redirect:/usuario", "erro", "Usuário não encontrado para exclusão.");
    }
}
