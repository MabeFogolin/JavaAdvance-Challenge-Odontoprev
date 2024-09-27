package com.fiap.N.I.B.gateways;


import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.gateways.requests.ProfissionalPatch;
import com.fiap.N.I.B.gateways.responses.ProfissionalPostResponse;
import com.fiap.N.I.B.usecases.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profissional")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    // Buscar profissional por registro
    @GetMapping("/registroProfissional/{registroProfissional}")
    public ResponseEntity<Profissional> buscarPorRegistro(@PathVariable String registroProfissional) {
        Optional<Profissional> profissional = profissionalService.buscarProfissional(registroProfissional);
        return profissional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar um novo profissional
    @PostMapping("/criar")
    public ResponseEntity<ProfissionalPostResponse> criarProfissional(@RequestBody Profissional profissionalParaCriar) {
        ProfissionalPostResponse respostaCriacao = profissionalService.criarProfissional(profissionalParaCriar);
        if (respostaCriacao.getMensagem().equals("Novo profissional cadastrado")) {
            return ResponseEntity.status(201).body(respostaCriacao);
        } else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
    }

    // Buscar todos os profissionais
    @GetMapping("/todos")
    public ResponseEntity<List<Profissional>> buscarTodos() {
        List<Profissional> todosProfissionais = profissionalService.buscarTodos();
        return ResponseEntity.ok(todosProfissionais);
    }

    // Buscar profissionais por categoria (tipoProfissional)
    @GetMapping("/categoria/{tipoProfissional}")
    public ResponseEntity<List<Profissional>> buscarPorCategoria(@PathVariable String tipoProfissional) {
        List<Profissional> profissionaisPorCategoria = profissionalService.buscarPorCategoria(tipoProfissional);
        return ResponseEntity.ok(profissionaisPorCategoria);
    }

    // Atualizar profissional
    @PutMapping("/atualizar/{registroProfissional}")
    public ResponseEntity<Profissional> atualizarProfissional(@PathVariable String registroProfissional,
                                                              @RequestBody Profissional profissionalParaAtualizar) {
        Optional<Profissional> profissionalAtualizado = profissionalService.atualizarProfissional(registroProfissional, profissionalParaAtualizar);
        return profissionalAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar email e telefone de um profissional
    @PatchMapping("/atualizar-email-telefone/{registroProfissional}")
    public ResponseEntity<Profissional> atualizarEmailTelefone(@PathVariable String registroProfissional,
                                                               @RequestBody ProfissionalPatch profissionalPatch) {
        Optional<Profissional> profissionalAtualizado = profissionalService.atualizarEmailTelefone(registroProfissional, profissionalPatch);
        return profissionalAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar profissional
    @DeleteMapping("/deletar/{registroProfissional}")
    public ResponseEntity<Void> deletarProfissional(@PathVariable String registroProfissional) {
        boolean deletado = profissionalService.deletarProfissional(registroProfissional);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}

