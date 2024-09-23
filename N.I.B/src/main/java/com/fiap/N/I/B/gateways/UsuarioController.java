package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.usecases.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Buscar um usuário por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Usuario> buscarPorCpf(@PathVariable String cpf) {
        Optional<Usuario> usuario = usuarioService.buscarPorCpf(cpf);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar usuários por plano
    @GetMapping("/plano/{planoUser}")
    public ResponseEntity<List<Usuario>> buscarPorPlano(@PathVariable String planoUser) {
        List<Usuario> usuarios = usuarioService.buscarPorPlano(planoUser);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> buscarUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por plano com paginação
    @GetMapping("/plano/{planoUser}/paginado")
    public ResponseEntity<Page<Usuario>> buscarPorPlanoPaginado(@PathVariable String planoUser, Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.buscarPorPlanoPaginado(planoUser, pageable);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por data de nascimento com paginação
    @GetMapping("/nascimento")
    public ResponseEntity<Page<Usuario>> buscarPorDataNascimento(@RequestParam Date dataNascimentoUser, Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.buscarPorDataNascimentoPaginado(dataNascimentoUser, pageable);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por data de nascimento (sem paginação)
    @GetMapping("/nascimento/{dataNascimento}")
    public ResponseEntity<List<Usuario>> getUsuariosPorDataNascimento(@PathVariable Date dataNascimento) {
        List<Usuario> usuarios = usuarioService.buscarPorDataNascimentoEmLista(dataNascimento);
        return ResponseEntity.ok(usuarios);
    }

    // Criar um novo usuário
    @PostMapping("/usuario/criar")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(201).body(novoUsuario);
    }

    // Atualizar um usuário existente
    @PutMapping("/cpf/{cpf}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable String cpf, @RequestBody Usuario usuarioAtualizado) {
        Optional<Usuario> usuario = usuarioService.atualizarUsuario(cpf, usuarioAtualizado);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar um usuário
    @DeleteMapping("/cpf/{cpf}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable String cpf) {
        boolean deleted = usuarioService.deletarUsuario(cpf);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
