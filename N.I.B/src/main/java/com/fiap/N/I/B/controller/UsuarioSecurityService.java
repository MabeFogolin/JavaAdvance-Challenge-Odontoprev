package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.model.Usuario;
import com.fiap.N.I.B.model.UsuarioSecurity;

import java.util.Optional;

public interface UsuarioSecurityService {

    Optional<Usuario> getUsuarioByCPF(String cpf);
    boolean deleteUsuarioByCPF(String cpf);
    String createUsuario(UsuarioSecurity usuario);
}

