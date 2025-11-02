package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Historico;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.Repositories.HistoricoRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usuarioThymeleaf")
@RequiredArgsConstructor
public class UsuarioThymeleafController {
        private final UsuarioRepository usuarioRepository;
    private final HistoricoRepository historicoRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        // Busca todos os usuários
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Para cada usuário, verifica se possui histórico
        for (Usuario u : usuarios) {
            Optional<Historico> historicoOpt = historicoRepository.findByUsuario_CpfUser(u.getCpfUser());
            u.setHistorico(historicoOpt.orElse(null));
        }


        // Ordena usuários: nota desc, sequência de dias desc, nome asc
        List<Usuario> usuariosOrdenados = usuarios.stream()
                .sorted(Comparator
                        .comparing(Usuario::getNota, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Usuario::getSequenciaDias, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Usuario::getNomeUser, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        // Adiciona ao model
        model.addAttribute("usuarios", usuariosOrdenados);

        return "usuarioThymeleaf";
    }


    @GetMapping("/{cpf}")
    public String listarUsuario(@PathVariable String cpf, Model model, RedirectAttributes redirectAttributes) {
        return usuarioRepository.findById(cpf).map(usuario -> {
            model.addAttribute("usuario", usuario);

            // ✅ Verifica se há histórico vinculado
            Optional<Historico> historicoOpt = historicoRepository.findByUsuario_CpfUser(usuario.getCpfUser());
            if (historicoOpt.isPresent()) {
                model.addAttribute("historico", historicoOpt.get());
            } else {
                model.addAttribute("historico", null);
            }

            return "listar-usuario"; // nome da página HTML
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("error", "Usuário não encontrado");
            return "redirect:/usuarioThymeleaf";
        });
    }


}
