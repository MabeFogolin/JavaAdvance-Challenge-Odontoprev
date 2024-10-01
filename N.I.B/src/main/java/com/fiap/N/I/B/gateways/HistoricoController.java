package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Historico;
import com.fiap.N.I.B.gateways.requests.HistoricoPatch;
import com.fiap.N.I.B.gateways.responses.HistoricoPostResponse;
import com.fiap.N.I.B.usecases.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    // Inserir novo histórico para um usuário
    @PostMapping("/inserir/{cpfUser}")
    public ResponseEntity<HistoricoPostResponse> inserirNoHistorico(@PathVariable String cpfUser, @RequestBody Historico historico) {
        HistoricoPostResponse resposta = historicoService.inserirNoHistorico(cpfUser, historico);
        if (resposta.getHistorico() != null) {
            return ResponseEntity.status(201).body(resposta);
        } else {
            return ResponseEntity.status(404).body(resposta);
        }
    }

    // Buscar histórico por CPF do usuário
    @GetMapping("/buscar/{cpfUser}")
    public ResponseEntity<Historico> buscarHistoricoPorUsuario(@PathVariable String cpfUser) {
        Optional<Historico> historico = historicoService.buscarHistoricoPorUsuario(cpfUser);
        return historico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar todo o histórico do usuário
    @PutMapping("/atualizar/{cpfUser}")
    public ResponseEntity<Historico> atualizarHistoricoCompleto(@PathVariable String cpfUser, @RequestBody Historico historicoParaAtualizar) {
        Optional<Historico> historicoAtualizado = historicoService.atualizarHistoricoCompleto(cpfUser, historicoParaAtualizar);
        return historicoAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar parcialmente o histórico do usuário (email, telefone, etc.)
    @PatchMapping("/atualizar-parcial/{cpfUser}")
    public ResponseEntity<Historico> atualizarInformacoesHistorico(@PathVariable String cpfUser, @RequestBody HistoricoPatch historicoPatch) {
        Optional<Historico> historicoAtualizado = historicoService.atualizarInformacoesHistorico(cpfUser, historicoPatch);
        return historicoAtualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar histórico do usuário por CPF
    @DeleteMapping("/deletar/{cpfUser}")
    public ResponseEntity<Void> deletarHistorico(@PathVariable String cpfUser) {
        boolean deletado = historicoService.deletarHistorico(cpfUser);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Listar todos os históricos
    @GetMapping("/todos")
    public ResponseEntity<List<Historico>> listarTodos() {
        List<Historico> historicos = historicoService.listarTodos();
        return ResponseEntity.ok(historicos);
    }
}
