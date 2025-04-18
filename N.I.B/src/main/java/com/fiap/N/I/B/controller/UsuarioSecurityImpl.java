package com.fiap.N.I.B.controller;

import com.fiap.N.I.B.Repositories.RoleRepository;
import com.fiap.N.I.B.Repositories.UsuarioSecurityRepository;
import com.fiap.N.I.B.model.RoleModel;
import com.fiap.N.I.B.model.RoleName;
import com.fiap.N.I.B.model.Usuario;
import com.fiap.N.I.B.model.UsuarioSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioSecurityImpl implements UsuarioSecurityService {

    private final UsuarioSecurityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UsuarioSecurityRepository usuarioSecurityRepository;

    @Override
    public Optional<Usuario> getUsuarioByCPF(String cpf) {
        return repository.findByCpfUser(cpf);
    }

    @Override
    public boolean deleteUsuarioByCPF(String cpf) {
        return repository.findByCpfUser(cpf)
                .map(usuario -> {
                    repository.delete(usuario);
                    return true;
                }).orElse(false);
    }
    public String createUsuario(UsuarioSecurity usuarioRequest) {
        Optional<Usuario> user = repository.findByEmailUser(usuarioRequest.getUsername());
        if (user.isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }
        UsuarioSecurity usuario = new UsuarioSecurity();
        usuario.setEmailUser(usuarioRequest.getUsername());
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.getPassword()));

        RoleModel userRole = roleRepository.findByRoleName(RoleName.valueOf(usuarioRequest.getRole().toString()))
                .orElseThrow(() -> new RuntimeException("Role não encontrada: " + usuarioRequest.getRole()));

        usuario.setRole(List.of(userRole));

        usuario = usuarioSecurityRepository.save(usuario);

        return usuario.getEmailUser();
    }
}
