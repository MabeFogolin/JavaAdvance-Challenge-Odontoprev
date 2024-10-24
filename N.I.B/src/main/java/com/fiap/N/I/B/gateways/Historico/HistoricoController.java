package com.fiap.N.I.B.gateways.Historico;

import com.fiap.N.I.B.domains.Historico;
import com.fiap.N.I.B.gateways.requests.HistoricoPatch;
import com.fiap.N.I.B.gateways.responses.HistoricoPostResponse;
import com.fiap.N.I.B.usecases.Historico.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/historico")
@RequiredArgsConstructor
public class HistoricoController {

    private final HistoricoService historicoService;

    // Inserir novo histórico para um usuário
    @PostMapping("/inserir/{cpfUser}")
    public ResponseEntity<EntityModel<HistoricoPostResponse>> inserirNoHistorico(@PathVariable String cpfUser, @RequestBody Historico historico) {
        HistoricoPostResponse resposta = historicoService.inserirNoHistorico(cpfUser, historico);

        if (resposta.getHistorico() != null) {
            EntityModel<HistoricoPostResponse> entityModel = EntityModel.of(resposta,
                    linkTo(methodOn(HistoricoController.class).buscarHistoricoPorUsuario(cpfUser)).withRel("buscar-historico"),
                    linkTo(methodOn(HistoricoController.class).listarTodos()).withRel("listar-todos"));

            return ResponseEntity.created(linkTo(methodOn(HistoricoController.class).inserirNoHistorico(cpfUser, historico)).toUri())
                    .body(entityModel);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Buscar histórico por CPF do usuário
    @GetMapping("/buscar/{cpfUser}")
    public ResponseEntity<EntityModel<Historico>> buscarHistoricoPorUsuario(@PathVariable String cpfUser) {
        Optional<Historico> historico = historicoService.buscarHistoricoPorUsuario(cpfUser);

        return historico.map(h -> {
            EntityModel<Historico> entityModel = EntityModel.of(h,
                    linkTo(methodOn(HistoricoController.class).buscarHistoricoPorUsuario(cpfUser)).withSelfRel(),
                    linkTo(methodOn(HistoricoController.class).listarTodos()).withRel("listar-todos"),
                    linkTo(methodOn(HistoricoController.class).atualizarHistoricoCompleto(cpfUser, h)).withRel("atualizar-historico"),
                    linkTo(methodOn(HistoricoController.class).deletarHistorico(cpfUser)).withRel("deletar-historico"));

            return ResponseEntity.ok(entityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar todo o histórico do usuário
    @PutMapping("/atualizar/{cpfUser}")
    public ResponseEntity<EntityModel<Historico>> atualizarHistoricoCompleto(@PathVariable String cpfUser, @RequestBody Historico historicoParaAtualizar) {
        Optional<Historico> historicoAtualizado = historicoService.atualizarHistoricoCompleto(cpfUser, historicoParaAtualizar);

        return historicoAtualizado.map(h -> {
            EntityModel<Historico> entityModel = EntityModel.of(h,
                    linkTo(methodOn(HistoricoController.class).buscarHistoricoPorUsuario(cpfUser)).withRel("buscar-historico"),
                    linkTo(methodOn(HistoricoController.class).listarTodos()).withRel("listar-todos"));

            return ResponseEntity.ok(entityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar parcialmente o histórico do usuário (email, telefone, etc.)
    @PatchMapping("/atualizar-parcial/{cpfUser}")
    public ResponseEntity<EntityModel<Historico>> atualizarInformacoesHistorico(@PathVariable String cpfUser, @RequestBody HistoricoPatch historicoPatch) {
        Optional<Historico> historicoAtualizado = historicoService.atualizarInformacoesHistorico(cpfUser, historicoPatch);

        return historicoAtualizado.map(h -> {
            EntityModel<Historico> entityModel = EntityModel.of(h,
                    linkTo(methodOn(HistoricoController.class).buscarHistoricoPorUsuario(cpfUser)).withRel("buscar-historico"),
                    linkTo(methodOn(HistoricoController.class).listarTodos()).withRel("listar-todos"));

            return ResponseEntity.ok(entityModel);
        }).orElseGet(() -> ResponseEntity.notFound().build());
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
    public ResponseEntity<List<EntityModel<Historico>>> listarTodos() {
        List<EntityModel<Historico>> historicos = historicoService.listarTodos().stream()
                .map(h -> EntityModel.of(h,
                        linkTo(methodOn(HistoricoController.class).buscarHistoricoPorUsuario(String.valueOf(h.getId()))).withRel("buscar-historico")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(historicos);
    }
}
