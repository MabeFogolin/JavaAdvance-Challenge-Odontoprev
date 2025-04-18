package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.configs.GetJwtToken;
import com.fiap.N.I.B.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/usuario/security")
@RequiredArgsConstructor
public class UsuarioSecurityController {

    private final GetJwtToken getJwtToken;
    private final UsuarioSecurityService usuarioSecurityService;

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
    public Optional<Usuario> getUsuario(@PathVariable String cpf) {
        return usuarioSecurityService.getUsuarioByCPF(cpf);
    }
}
