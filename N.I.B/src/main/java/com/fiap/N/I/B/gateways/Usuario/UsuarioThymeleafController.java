package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarioThymeleaf")
@RequiredArgsConstructor
public class UsuarioThymeleafController {
        private final UsuarioRepository usuarioRepository;

        @GetMapping
        public String listarUsuarios(Model model) {
            List<Usuario> usuarios = usuarioRepository.findAll();
            model.addAttribute("usuarios", usuarios);
            return "usuarioThymeleaf";
        }

}
