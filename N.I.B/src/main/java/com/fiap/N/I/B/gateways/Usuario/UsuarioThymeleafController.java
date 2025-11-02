package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarioThymeleaf")
@RequiredArgsConstructor
public class UsuarioThymeleafController {
        private final UsuarioRepository usuarioRepository;

        @GetMapping
        public String listarUsuarios(Model model) {
            List<Usuario> usuarios = usuarioRepository.findAll();
            List<Usuario> usuariosOrdenados = usuarios.stream()
                    .sorted(Comparator
                            .comparing(Usuario::getNota, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(Usuario::getSequenciaDias, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(Usuario::getNomeUser, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            model.addAttribute("usuarios", usuariosOrdenados);
            return "usuarioThymeleaf";
        }

    @GetMapping("/{cpf}")
    public String listarUsuario(@PathVariable String cpf, Model model, RedirectAttributes redirectAttributes) {
        return usuarioRepository.findById(cpf).map(usuario -> {
            model.addAttribute("usuario", usuario);
            return "listar-usuario";
        }).orElseGet(() -> {
            return "redirect:/usuarioThymeleaf";
        });
    }

}
