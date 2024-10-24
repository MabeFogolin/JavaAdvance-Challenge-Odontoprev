package com.fiap.N.I.B.gateways.Diario;

import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;
import com.fiap.N.I.B.usecases.Diario.DiarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/diario")
@RequiredArgsConstructor
public class DiarioController {

    private final DiarioService diarioService;

    // Criar novo registro no diário
    @PostMapping("/criar")
    public ResponseEntity<EntityModel<DiarioPostResponse>> criarRegistro(
            @RequestParam String cpfUser,
            @RequestBody Diario diarioParaCriar) {
        DiarioPostResponse respostaCriacao = diarioService.inserirNoDiario(cpfUser, diarioParaCriar);
        if (respostaCriacao.getMensagem().equals("Novo registro adicionado ao diário")) {
            EntityModel<DiarioPostResponse> resource = EntityModel.of(respostaCriacao);
            resource.add(linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"));
            return ResponseEntity.status(201).body(resource);
        } else {
            return ResponseEntity.status(409).body(EntityModel.of(respostaCriacao));
        }
    }

    // Buscar registros do diário por usuário
    @GetMapping("/usuario")
    public ResponseEntity<CollectionModel<EntityModel<Diario>>> buscarRegistrosPorUsuario(@RequestParam String cpfUser) {
        List<Diario> registros = diarioService.buscarRegistrosPorUsuario(cpfUser);
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Diario>> registrosModel = registros.stream()
                    .map(diario -> EntityModel.of(diario,
                            linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withSelfRel(),
                            linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios")
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Diario>> collectionModel = CollectionModel.of(registrosModel);
            collectionModel.add(linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Buscar todos os registros do diário
    @GetMapping("/todos")
    public ResponseEntity<CollectionModel<EntityModel<Diario>>> buscarTodosRegistros() {
        List<Diario> registros = diarioService.buscarTodos();
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Diario>> registrosModel = registros.stream()
                    .map(diario -> EntityModel.of(diario,
                            linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withSelfRel()
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Diario>> collectionModel = CollectionModel.of(registrosModel);
            collectionModel.add(linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withSelfRel());
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Atualizar um registro do diário
    @PutMapping("/atualizar")
    public ResponseEntity<EntityModel<Diario>> atualizarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody Diario diarioParaAtualizar) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarRegistro(cpfUser, dataRegistro, diarioParaAtualizar);
        if (diarioAtualizado.isPresent()) {
            EntityModel<Diario> resource = EntityModel.of(diarioAtualizado.get(),
                    linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"),
                    linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar informações específicas de um registro (patch)
    @PatchMapping("/atualizar-informacoes")
    public ResponseEntity<EntityModel<Diario>> atualizarInformacoesRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody DiarioPatch diarioPatch) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarInformacoesRegistro(cpfUser, dataRegistro, diarioPatch);
        if (diarioAtualizado.isPresent()) {
            EntityModel<Diario> resource = EntityModel.of(diarioAtualizado.get(),
                    linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"),
                    linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar um registro do diário
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro) {
        boolean deletado = diarioService.deletarRegistro(cpfUser, dataRegistro);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
