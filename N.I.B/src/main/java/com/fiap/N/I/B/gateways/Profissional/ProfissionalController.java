package com.fiap.N.I.B.gateways.Profissional;

import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.gateways.requests.ProfissionalPatch;
import com.fiap.N.I.B.gateways.responses.ProfissionalPostResponse;
import com.fiap.N.I.B.usecases.Profissional.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/profissional")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    // Buscar profissional por registro
    @GetMapping("/registroProfissional/{registroProfissional}")
    public ResponseEntity<EntityModel<Profissional>> buscarPorRegistro(@PathVariable String registroProfissional) {
        Optional<Profissional> profissional = profissionalService.buscarProfissional(registroProfissional);

        return profissional.map(p -> {
            EntityModel<Profissional> resource = EntityModel.of(p);
            Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(registroProfissional)).withSelfRel();
            Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");
            resource.add(selfLink);
            resource.add(allProfessionalsLink);
            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Criar um novo profissional
    @PostMapping("/criar")
    public ResponseEntity<EntityModel<ProfissionalPostResponse>> criarProfissional(@RequestBody Profissional profissionalParaCriar) {
        ProfissionalPostResponse respostaCriacao = profissionalService.criarProfissional(profissionalParaCriar);

        EntityModel<ProfissionalPostResponse> resource = EntityModel.of(respostaCriacao);
        Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(profissionalParaCriar.getRegistroProfissional())).withSelfRel();
        Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");

        resource.add(selfLink);
        resource.add(allProfessionalsLink);

        if (respostaCriacao.getMensagem().equals("Novo profissional cadastrado")) {
            return ResponseEntity.status(201).body(resource);
        } else {
            return ResponseEntity.status(409).body(resource);
        }
    }

    // Buscar todos os profissionais
    @GetMapping("/todos")
    public ResponseEntity<List<EntityModel<Profissional>>> buscarTodos() {
        List<Profissional> todosProfissionais = profissionalService.buscarTodos();

        List<EntityModel<Profissional>> todosProfissionaisComLink = todosProfissionais.stream().map(profissional -> {
            EntityModel<Profissional> resource = EntityModel.of(profissional);
            Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(profissional.getRegistroProfissional())).withSelfRel();
            Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");

            resource.add(selfLink);
            resource.add(allProfessionalsLink);

            return resource;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(todosProfissionaisComLink);
    }

    // Buscar profissionais por categoria (tipoProfissional)
    @GetMapping("/categoria/{tipoProfissional}")
    public ResponseEntity<List<EntityModel<Profissional>>> buscarPorCategoria(@PathVariable String tipoProfissional) {
        List<Profissional> profissionaisPorCategoria = profissionalService.buscarPorCategoria(tipoProfissional);

        List<EntityModel<Profissional>> profissionaisComLink = profissionaisPorCategoria.stream().map(profissional -> {
            EntityModel<Profissional> resource = EntityModel.of(profissional);
            Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(profissional.getRegistroProfissional())).withSelfRel();
            Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");

            resource.add(selfLink);
            resource.add(allProfessionalsLink);

            return resource;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(profissionaisComLink);
    }

    // Atualizar profissional
    @PutMapping("/atualizar/{registroProfissional}")
    public ResponseEntity<EntityModel<Profissional>> atualizarProfissional(@PathVariable String registroProfissional,
                                                                           @RequestBody Profissional profissionalParaAtualizar) {
        Optional<Profissional> profissionalAtualizado = profissionalService.atualizarProfissional(registroProfissional, profissionalParaAtualizar);

        return profissionalAtualizado.map(p -> {
            EntityModel<Profissional> resource = EntityModel.of(p);
            Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(registroProfissional)).withSelfRel();
            Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");

            resource.add(selfLink);
            resource.add(allProfessionalsLink);

            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar email e telefone de um profissional
    @PatchMapping("/atualizar-email-telefone/{registroProfissional}")
    public ResponseEntity<EntityModel<Profissional>> atualizarEmailTelefone(@PathVariable String registroProfissional,
                                                                            @RequestBody ProfissionalPatch profissionalPatch) {
        Optional<Profissional> profissionalAtualizado = profissionalService.atualizarEmailTelefone(registroProfissional, profissionalPatch);

        return profissionalAtualizado.map(p -> {
            EntityModel<Profissional> resource = EntityModel.of(p);
            Link selfLink = linkTo(methodOn(ProfissionalController.class).buscarPorRegistro(registroProfissional)).withSelfRel();
            Link allProfessionalsLink = linkTo(methodOn(ProfissionalController.class).buscarTodos()).withRel("all-professionals");

            resource.add(selfLink);
            resource.add(allProfessionalsLink);

            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar profissional
    @DeleteMapping("/deletar/{registroProfissional}")
    public ResponseEntity<Void> deletarProfissional(@PathVariable String registroProfissional) {
        boolean deletado = profissionalService.deletarProfissional(registroProfissional);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
