package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.gateways.Repositories.DiarioRepository;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import com.fiap.N.I.B.model.Consulta;
import com.fiap.N.I.B.model.Diario;
import com.fiap.N.I.B.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/diarios")
@RequiredArgsConstructor
public class DiarioController {

    private final DiarioRepository diarioRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/novo")
    public ModelAndView novoDiarioForm() {
        Diario diarioVazio = new Diario();
        return new ModelAndView("Diarios/cadastrar-diario", "diario", diarioVazio);
    }

    @PostMapping("/novo")
    public ModelAndView novoDiario(@ModelAttribute Diario diario) {
        Optional<Usuario> usuario = usuarioRepository.findByCpfUser(diario.getUsuario().getCpfUser());

        if (usuario.isPresent()) {
            Usuario usuarioExistente = usuario.get();

            Diario novoDiario = Diario.builder()
                    .id(diario.getId())
                    .dataRegistro(diario.getDataRegistro() != null ? diario.getDataRegistro() : LocalDate.now())
                    .sintomaDiario(diario.getSintomaDiario())
                    .escovacaoDiario(diario.getEscovacaoDiario())
                    .usoFioDiario(diario.getUsoFioDiario())
                    .usoEnxaguanteDiario(diario.getUsoEnxaguanteDiario())
                    .usuario(usuarioExistente)
                    .build();

            diarioRepository.save(novoDiario);

            usuarioExistente.getDiarios().add(novoDiario);
            usuarioRepository.save(usuarioExistente);

            return new ModelAndView("redirect:/diarios", "sucesso", "Registro no diário salvo com sucesso!");
        } else {
            return new ModelAndView("Diarios/cadastrar-diario", "erro", "Usuário não encontrado.");
        }
    }

    @GetMapping
    public ModelAndView listarDiarios() {
        List<Diario> diarios = diarioRepository.findAll();
        return new ModelAndView("Diarios/lista", "diarios", diarios);
    }

    @GetMapping("/{id}")
    public ModelAndView listarDiario(@PathVariable Long id) {
        Optional<Diario> diarioOptional = diarioRepository.findById(id);
        if (diarioOptional.isPresent()) {
            return new ModelAndView("Diarios/listar-diario", "diario", diarioOptional.get());
        }
        return new ModelAndView("redirect:/diarios", "erro", "Registro diário não encontrada.");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarDiarioForm(@PathVariable Long id) {
        Optional<Diario> diarioOptional = diarioRepository.findById(id);
        if (diarioOptional.isPresent()) {
            Diario diarioEditar = diarioOptional.get();
            return new ModelAndView("Diarios/editar-diario", "diario", diarioEditar);
        }
        return new ModelAndView("redirect:/diarios", "erro", "Registro do diário não encontrado.");
    }

    @PostMapping("/editar")
    public ModelAndView atualizarDiario(@ModelAttribute Diario diarioParam) {
        Optional<Diario> diarioOptional = diarioRepository.findById(diarioParam.getId());

        if (diarioOptional.isPresent()) {
            Diario diarioTransacao = diarioOptional.get();
            Diario diarioAtualizado = Diario.builder()
                    .id(diarioParam.getId())
                    .dataRegistro(diarioTransacao.getDataRegistro())
                    .sintomaDiario(diarioParam.getSintomaDiario())
                    .escovacaoDiario(diarioParam.getEscovacaoDiario())
                    .usoFioDiario(diarioParam.getUsoFioDiario())
                    .usoEnxaguanteDiario(diarioParam.getUsoEnxaguanteDiario())
                    .usuario(diarioTransacao.getUsuario())
                    .build();

            diarioRepository.save(diarioAtualizado);
            return new ModelAndView("redirect:/diarios", "sucesso", "Registro do diário atualizado com sucesso!");
        }

        return new ModelAndView("Diarios/editar-diario", "erro", "Erro ao atualizar o registro.");
    }

    @GetMapping("/deletar/{id}")
    public ModelAndView deletarDiario(@PathVariable Long id) {
        Optional<Diario> diarioOptional = diarioRepository.findById(id);
        if (diarioOptional.isPresent()) {
            diarioRepository.deleteById(id);
            return new ModelAndView("redirect:/diarios", "sucesso", "Registro deletado com sucesso!");
        }
        return new ModelAndView("redirect:/diarios", "erro", "Registro não encontrado para exclusão.");
    }
}
