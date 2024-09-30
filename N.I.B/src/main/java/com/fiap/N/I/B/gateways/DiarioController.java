package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;
import com.fiap.N.I.B.usecases.DiarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/diario")
@RequiredArgsConstructor
public class DiarioController {

    private final DiarioService diarioService;

    // Criar novo registro no diário
    @PostMapping("/criar/{cpfUser}")
    public ResponseEntity<DiarioPostResponse> criarRegistro(@PathVariable String cpfUser, @RequestBody Diario diarioParaCriar) {
        DiarioPostResponse respostaCriacao = diarioService.inserirNoDiario(cpfUser, diarioParaCriar);
        if (respostaCriacao.getMensagem().equals("Novo registro adicionado ao diário")) {
            return ResponseEntity.status(201).body(respostaCriacao);
        } else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
    }

    // Buscar registros do diário por usuário
    @GetMapping("/usuario/{cpfUser}")
    public ResponseEntity<List<Diario>> buscarRegistrosPorUsuario(@PathVariable String cpfUser) {
        List<Diario> registros = diarioService.buscarRegistrosPorUsuario(cpfUser);
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(registros);
        }
    }

    // Buscar registros do diário por usuário
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
    @PutMapping("/atualizar/{cpfUser}/{dataRegistro}")
    public ResponseEntity<Diario> atualizarRegistro(@PathVariable String cpfUser,
                                                    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataRegistro,
                                                    @RequestBody Diario diarioParaAtualizar) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarRegistro(cpfUser, dataRegistro, diarioParaAtualizar);
        return diarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Atualizar informações específicas de um registro (patch)
    @PatchMapping("/atualizar-informacoes/{cpfUser}/{dataRegistro}")
    public ResponseEntity<Diario> atualizarInformacoesRegistro(@PathVariable String cpfUser,
                                                               @PathVariable Date dataRegistro,
                                                               @RequestBody DiarioPatch diarioPatch) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarInformacoesRegistro(cpfUser, dataRegistro, diarioPatch);
        return diarioAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar um registro do diário
    @DeleteMapping("/deletar/{cpfUser}/{dataRegistro}")
    public ResponseEntity<Void> deletarRegistro(@PathVariable String cpfUser, @PathVariable Date dataRegistro) {
        boolean deletado = diarioService.deletarRegistro(cpfUser, dataRegistro);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
