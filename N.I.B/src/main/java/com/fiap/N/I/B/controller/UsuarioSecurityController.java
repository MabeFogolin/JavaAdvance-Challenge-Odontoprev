package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.configs.GetJwtToken;
import com.fiap.N.I.B.ignore.usecases.Usuario.UsuarioService;
import com.fiap.N.I.B.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuario/security")
@RequiredArgsConstructor
public class UsuarioSecurityController {

    private final GetJwtToken getJwtToken;
    private final UsuarioSecurityService usuarioSecurityService;
    private final UsuarioService usuarioService;

    @GetMapping()
    public String getJwt(Authentication authentication) {
        return "Olá, JWT válido " + getJwtToken.execute(authentication);
    }

    @Operation(summary = "Buscar usuário por cpf", description = "Retorna as informações do usuário com base no CPF fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{cpf}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUsuario(@PathVariable String cpf) {
        Optional<Usuario> usuario = usuarioSecurityService.getUsuarioByCPF(cpf);

        return usuario.map(u -> ResponseEntity.ok(u))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Busca todos os USUÁRIOS", description = "Traz todos os USUÁRIOS cadastrados, com os links atribuídos individualmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", links = {
                    @io.swagger.v3.oas.annotations.links.Link(name = "teste", operationRef = "GET")
            }),
            @ApiResponse(responseCode = "404")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/todos")
    public ResponseEntity<List<EntityModel<Usuario>>> buscarUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodos();

        List<EntityModel<Usuario>> usuarioModels = usuarios.stream()
                .map(usuario -> EntityModel.of(usuario)) 
                .collect(Collectors.toList());

        return ResponseEntity.ok(usuarioModels);
    }

}
