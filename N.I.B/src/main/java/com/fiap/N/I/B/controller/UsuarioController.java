package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import com.fiap.N.I.B.model.Profissional;
import com.fiap.N.I.B.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/novo")
    public ModelAndView novoUsuarioForm() {
        Usuario usuarioVazio = new Usuario();
        return new ModelAndView("Usuario/cadastrar-usuario", "usuario", usuarioVazio);
    }

    @PostMapping("/novo")
    public ModelAndView novoUsuario(@ModelAttribute Usuario usuarioParam) {
        // Busca o usuário com o CPF fornecido
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(usuarioParam.getCpfUser());

        if (usuario.isPresent()) {
            return new ModelAndView("Usuario/cadastrar-usuario", "erro", "CPF já cadastrado.");
        } else {
            Usuario newUsuario = Usuario.builder()
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
            usuarioRepository.save(newUsuario);

            return new ModelAndView("redirect:/usuario", "sucesso", "Usuário cadastrado com sucesso!");
        }
    }

    @GetMapping
    public ModelAndView listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return new ModelAndView("Usuario/lista", "usuarios", usuarios);
    }

    @GetMapping("/editar/{cpf}")
    public ModelAndView editarUsuarioForm(@PathVariable String cpf) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cpf);
        if (usuarioOptional.isPresent()) {
            return new ModelAndView("Usuario/editar-usuario", "usuario", usuarioOptional.get());
        }
        return new ModelAndView("redirect:/usuario", "erro", "Usuário não encontrado.");
    }

    @GetMapping("/{cpf}")
    public ModelAndView listarUsuario(@PathVariable String cpf) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(cpf);
        if (usuarioOptional.isPresent()) {
            return new ModelAndView("Usuario/listar-usuario", "usuario", usuarioOptional.get());
        }
        return new ModelAndView("redirect:/usuario", "erro", "Usuário não encontrado.");
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

            usuarioRepository.save(usuarioAtualizado);
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
