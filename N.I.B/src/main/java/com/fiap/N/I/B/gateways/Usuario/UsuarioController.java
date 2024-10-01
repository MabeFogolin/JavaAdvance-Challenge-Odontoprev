package com.fiap.N.I.B.gateways.Usuario;

import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.UsuarioPatch;
import com.fiap.N.I.B.gateways.responses.UsuarioPostResponse;
import com.fiap.N.I.B.usecases.Usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
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

    //Buscar todos os usuários
    @GetMapping("/todos")
    public ResponseEntity<List<Usuario>> buscarUsuarios() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por plano com paginação
    @GetMapping("/plano")
    public ResponseEntity<Page<Usuario>> buscarPorPlanoPaginado(@RequestParam String planoUser, Pageable page) {
        Page<Usuario> usuarios = usuarioService.buscarPorPlanoPaginado(planoUser, page);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por data de nascimento com paginação
    @GetMapping("/nascimento")
    public ResponseEntity<Page<Usuario>> buscarPorDataNascimento(@RequestParam String dataNascimentoUser, Pageable page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dataNascimentoUser, formatter);
        Page<Usuario> usuarios = usuarioService.buscarPorDataNascimentoPaginado(Date.valueOf(date), page);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuários por data de nascimento (sem paginação)
    @GetMapping("/nascimento/{dataNascimento}")
    public ResponseEntity<List<Usuario>> getUsuariosPorDataNascimento(@PathVariable Date dataNascimento) {
        List<Usuario> usuarios = usuarioService.buscarPorDataNascimentoEmLista(dataNascimento);
        return ResponseEntity.ok(usuarios);
    }

    // Criar um novo usuário
    @PostMapping("/criar")
    public ResponseEntity<UsuarioPostResponse> criarUsuario(@RequestBody Usuario usuario) {
        UsuarioPostResponse respostaCriacao = usuarioService.criarUsuario(usuario);
        if(respostaCriacao.getMensagem().equals("Novo usuário cadastrado")){
            return ResponseEntity.status(201).body(respostaCriacao);
        }
        else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
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

    @PatchMapping("/{cpfUser}/atualizar")
    public ResponseEntity<Usuario> atualizarPlanoEmail(
            @PathVariable String cpfUser,
            @RequestBody @Valid UsuarioPatch userEmailPlano) {

        Optional<Usuario> usuarioAtualizado = usuarioService.atualizarEmailPlano(cpfUser, userEmailPlano);

        return usuarioAtualizado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
