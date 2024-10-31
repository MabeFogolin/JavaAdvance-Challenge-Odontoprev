package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.UsuarioPatch;
import com.fiap.N.I.B.gateways.responses.UsuarioPostResponse;
import com.fiap.N.I.B.usecases.Usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Buscar um usuário por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<EntityModel<Usuario>> buscarPorCpf(@PathVariable String cpf) {
        Optional<Usuario> usuario = usuarioService.buscarPorCpf(cpf);

        return usuario.map(u -> {
            EntityModel<Usuario> resource = EntityModel.of(u);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(cpf)).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");
            resource.add(selfLink);
            resource.add(allUsersLink);
            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/plano/{planoUser}")
    public ResponseEntity<List<EntityModel<Usuario>>> buscarPorPlano(@PathVariable String planoUser) {
        List<Usuario> usuarios = usuarioService.buscarPorPlano(planoUser);

        List<EntityModel<Usuario>> usuariosPorPlanoComLinks = usuarios.stream().map(usuario -> {
            EntityModel<Usuario> resource = EntityModel.of(usuario);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(usuario.getCpfUser())).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");
            resource.add(selfLink);
            resource.add(allUsersLink);
            return resource;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usuariosPorPlanoComLinks);
    }

    @Operation(summary = "Busca todos os alunos", description = "Traz todos os alunos cadastrados, com os links atribuídos individualmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", links = {
                    @io.swagger.v3.oas.annotations.links.Link(name = "teste", operationRef = "GET")
            }),
            @ApiResponse(responseCode = "404")
    })
    @GetMapping("/todos")
    public ResponseEntity<List<EntityModel<Usuario>>> buscarUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodos();

        List<EntityModel<Usuario>> todosUsuariosComLink = usuarios.stream().map(usuario -> {
            EntityModel<Usuario> resource = EntityModel.of(usuario);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(usuario.getCpfUser())).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");

            resource.add(selfLink);
            resource.add(allUsersLink);

            return resource;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(todosUsuariosComLink);
    }

    // Buscar usuários por data de nascimento (sem paginação)
    @GetMapping("/nascimento/{dataNascimento}")
    public ResponseEntity<List<EntityModel<Usuario>>> getUsuariosPorDataNascimento(@PathVariable LocalDate dataNascimento) {
        List<Usuario> usuarios = usuarioService.buscarPorDataNascimentoEmLista(dataNascimento);

        List<EntityModel<Usuario>> usuariosComLinks = usuarios.stream().map(usuario -> {
            EntityModel<Usuario> resource = EntityModel.of(usuario);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(usuario.getCpfUser())).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");

            resource.add(selfLink);
            resource.add(allUsersLink);

            return resource;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usuariosComLinks);
    }

    // Criar um novo usuário
    @PostMapping("/criar")
    public ResponseEntity<EntityModel<UsuarioPostResponse>> criarUsuario(@RequestBody Usuario usuario) {
        UsuarioPostResponse respostaCriacao = usuarioService.criarUsuario(usuario);

        EntityModel<UsuarioPostResponse> resource = EntityModel.of(respostaCriacao);
        Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(usuario.getCpfUser())).withSelfRel();
        Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");

        resource.add(selfLink);
        resource.add(allUsersLink);
        if ("CPF já cadastrado no sistema".equals(respostaCriacao.getMensagem())) {
            return ResponseEntity.status(409).body(resource);
        }
        return ResponseEntity.status(201).body(resource);
    }

    // Atualizar um usuário por CPF
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable String cpf, @RequestBody Usuario usuarioAtualizado) {
        Optional<Usuario> usuario = usuarioService.atualizarUsuario(cpf, usuarioAtualizado);

        return usuario.map(u -> {
            EntityModel<Usuario> resource = EntityModel.of(u);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(cpf)).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");

            resource.add(selfLink);
            resource.add(allUsersLink);

            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar plano e email do usuário
    @PatchMapping("/{cpfUser}/atualizar")
    public ResponseEntity<EntityModel<Usuario>> atualizarPlanoEmail(
            @PathVariable String cpfUser,
            @RequestBody @Valid UsuarioPatch userEmailPlano) {

        Optional<Usuario> usuarioAtualizado = usuarioService.atualizarEmailPlano(cpfUser, userEmailPlano);

        return usuarioAtualizado.map(u -> {
            EntityModel<Usuario> resource = EntityModel.of(u);
            Link selfLink = linkTo(methodOn(UsuarioController.class).buscarPorCpf(cpfUser)).withSelfRel();
            Link allUsersLink = linkTo(methodOn(UsuarioController.class).buscarUsuarios()).withRel("all-users");

            resource.add(selfLink);
            resource.add(allUsersLink);

            return ResponseEntity.ok(resource);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String cpf) {
        boolean deleted = usuarioService.deletarUsuario(cpf);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
