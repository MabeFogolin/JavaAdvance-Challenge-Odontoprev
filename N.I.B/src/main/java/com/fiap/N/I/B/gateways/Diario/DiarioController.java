package com.fiap.N.I.B.gateways.Diario;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;
import com.fiap.N.I.B.usecases.Diario.DiarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diario")
@RequiredArgsConstructor
public class DiarioController {

    private final DiarioService diarioService;

    // Criar novo registro no diário
    @PostMapping("/criar")
    public ResponseEntity<DiarioPostResponse> criarRegistro(
            @RequestParam String cpfUser,
            @RequestBody Diario diarioParaCriar) {
        DiarioPostResponse respostaCriacao = diarioService.inserirNoDiario(cpfUser, diarioParaCriar);
        if (respostaCriacao.getMensagem().equals("Novo registro adicionado ao diário")) {
            return ResponseEntity.status(201).body(respostaCriacao);
        } else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
    }

    // Buscar registros do diário por usuário
    @GetMapping("/usuario")
    public ResponseEntity<List<Diario>> buscarRegistrosPorUsuario(@RequestParam String cpfUser) {
        List<Diario> registros = diarioService.buscarRegistrosPorUsuario(cpfUser);
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(registros);
        }
    }

    // Buscar todos os registros do diário
    @GetMapping("/todos")
    public ResponseEntity<List<Diario>> buscarTodosRegistros() {
        List<Diario> registros = diarioService.buscarTodos();
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(registros);
        }
    }

    // Atualizar um registro do diário
    @PutMapping("/atualizar")
    public ResponseEntity<Diario> atualizarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody Diario diarioParaAtualizar) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarRegistro(cpfUser, dataRegistro, diarioParaAtualizar);
        return diarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar informações específicas de um registro (patch)
    @PatchMapping("/atualizar-informacoes")
    public ResponseEntity<Diario> atualizarInformacoesRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody DiarioPatch diarioPatch) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarInformacoesRegistro(cpfUser, dataRegistro, diarioPatch);
        return diarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar um registro do diário
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro) {
        boolean deletado = diarioService.deletarRegistro(cpfUser, dataRegistro);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
