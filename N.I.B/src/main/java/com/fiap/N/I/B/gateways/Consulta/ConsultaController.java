package com.fiap.N.I.B.gateways.Consulta;

import com.fiap.N.I.B.domains.Consulta;
import com.fiap.N.I.B.gateways.requests.ConsultaPatch;
import com.fiap.N.I.B.gateways.responses.ConsultaPostResponse;
import com.fiap.N.I.B.usecases.Consulta.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    // Criar nova consulta
    @PostMapping("/criar")
    public ResponseEntity<EntityModel<ConsultaPostResponse>> criarConsulta(@RequestParam String cpfUser,
                                                                           @RequestParam String registroProfissional,
                                                                           @RequestBody Consulta consultaParaInserir) {
        ConsultaPostResponse respostaCriacao = consultaService.criarConsulta(cpfUser, registroProfissional, consultaParaInserir);
        if (respostaCriacao.getMensagem().equals("Nova consulta adicionada")) {
            EntityModel<ConsultaPostResponse> resource = EntityModel.of(respostaCriacao);
            resource.add(linkTo(methodOn(ConsultaController.class).buscarConsultasPorUsuario(cpfUser)).withRel("usuario-consultas"));
            return ResponseEntity.status(201).body(resource);
        } else {
            return ResponseEntity.status(409).body(EntityModel.of(respostaCriacao));
        }
    }

    // Buscar consultas por usuário
    @GetMapping("/usuario")
    public ResponseEntity<CollectionModel<EntityModel<Consulta>>> buscarConsultasPorUsuario(@RequestParam String cpfUser) {
        List<Consulta> consultas = consultaService.consultasPorUsuario(cpfUser);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Consulta>> consultasModel = consultas.stream()
                    .map(consulta -> EntityModel.of(consulta,
                            linkTo(methodOn(ConsultaController.class).buscarConsultasPorUsuario(cpfUser)).withSelfRel(),
                            linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas")
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Consulta>> collectionModel = CollectionModel.of(consultasModel);
            collectionModel.add(linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas"));
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Buscar consultas por profissional
    @GetMapping("/profissional")
    public ResponseEntity<CollectionModel<EntityModel<Consulta>>> buscarConsultasPorProfissional(@RequestParam String registroProfissional) {
        List<Consulta> consultas = consultaService.consultasPorProfissional(registroProfissional);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Consulta>> consultasModel = consultas.stream()
                    .map(consulta -> EntityModel.of(consulta,
                            linkTo(methodOn(ConsultaController.class).buscarConsultasPorProfissional(registroProfissional)).withSelfRel(),
                            linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas")
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Consulta>> collectionModel = CollectionModel.of(consultasModel);
            collectionModel.add(linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas"));
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Atualizar uma consulta (PUT)
    @PutMapping("/atualizar")
    public ResponseEntity<EntityModel<Consulta>> atualizarConsulta(@RequestParam String cpfUser,
                                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta,
                                                                   @RequestBody Consulta consultaParaAtualizar) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarConsultaTotalmente(cpfUser, dataConsulta, consultaParaAtualizar);
        return consultaAtualizada.map(consulta -> {
            EntityModel<Consulta> resource = EntityModel.of(consulta,
                    linkTo(methodOn(ConsultaController.class).buscarConsultasPorUsuario(cpfUser)).withRel("usuario-consultas"),
                    linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas"));
            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar informações específicas de uma consulta (PATCH)
    @PatchMapping("/atualizar-informacoes")
    public ResponseEntity<EntityModel<Consulta>> atualizarInformacoesConsulta(@RequestParam String cpfUser,
                                                                              @RequestParam String registroProfissional,
                                                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta,
                                                                              @RequestBody ConsultaPatch consultaPatch) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarInformacoesConsulta(cpfUser, registroProfissional, dataConsulta, consultaPatch);
        return consultaAtualizada.map(consulta -> {
            EntityModel<Consulta> resource = EntityModel.of(consulta,
                    linkTo(methodOn(ConsultaController.class).buscarConsultasPorUsuario(cpfUser)).withRel("usuario-consultas"),
                    linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas"));
            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar uma consulta
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarConsulta(@RequestParam String cpfUser,
                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta) {
        boolean deletado = consultaService.deletarRegistro(cpfUser, dataConsulta);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Buscar todos os registros de consultas
    @GetMapping("/todos")
    public ResponseEntity<CollectionModel<EntityModel<Consulta>>> buscarTodasConsultas() {
        List<Consulta> consultas = consultaService.todosRegistros();
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Consulta>> consultasModel = consultas.stream()
                    .map(consulta -> EntityModel.of(consulta,
                            linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withSelfRel()
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Consulta>> collectionModel = CollectionModel.of(consultasModel);
            collectionModel.add(linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withSelfRel());
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Buscar consulta por usuário e data
    @GetMapping("/usuario/data")
    public ResponseEntity<EntityModel<Consulta>> buscarConsultaPorData(@RequestParam String cpfUser,
                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta) {
        Optional<Consulta> consulta = consultaService.buscarConsultaPorData(cpfUser, dataConsulta);
        return consulta.map(c -> {
            EntityModel<Consulta> resource = EntityModel.of(c,
                    linkTo(methodOn(ConsultaController.class).buscarConsultasPorUsuario(cpfUser)).withRel("usuario-consultas"),
                    linkTo(methodOn(ConsultaController.class).buscarTodasConsultas()).withRel("todas-consultas"));
            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
