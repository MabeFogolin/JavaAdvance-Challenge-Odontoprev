package com.fiap.N.I.B.gateways.Endereco;

import com.fiap.N.I.B.domains.Endereco;
import com.fiap.N.I.B.gateways.requests.EnderecoPatch;
import com.fiap.N.I.B.gateways.responses.EnderecoPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoServiceImpl enderecoService;

    // Criação de um novo endereço
    @PostMapping("/criar/{idPessoa}")
    public ResponseEntity<EnderecoPostResponse> criarEndereco(@PathVariable String idPessoa, @RequestBody Endereco endereco) {
        EnderecoPostResponse response = enderecoService.criarEndereco(idPessoa, endereco);
        if (response.getEndereco() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Atualização parcial de um endereço
    @PatchMapping("/{idPessoa}")
    public ResponseEntity<Endereco> atualizarParcial(@PathVariable String idPessoa, @RequestBody EnderecoPatch enderecoPatch) {
        Optional<Endereco> enderecoAtualizado = enderecoService.atualizarParcial(idPessoa, enderecoPatch);
        return enderecoAtualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Atualização total de um endereço
    @PutMapping("/atualizar/{idPessoa}")
    public ResponseEntity<Endereco> atualizarTotalmente(@PathVariable String idPessoa, @RequestBody Endereco endereco) {
        Optional<Endereco> enderecoAtualizado = enderecoService.atualizarTotalmente(idPessoa, endereco);
        return enderecoAtualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Deletar endereço
    @DeleteMapping("/deletar/{idPessoa}")
    public ResponseEntity<Void> deletarEndereco(@PathVariable String idPessoa) {
        boolean deletado = enderecoService.deletarEndereco(idPessoa);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Buscar endereço por profissional
    @GetMapping("/profissional/{registroProfissional}")
    public ResponseEntity<Endereco> buscarEnderecoPorProfissional(@PathVariable String registroProfissional) {
        Optional<Endereco> endereco = enderecoService.buscarEnderecoPorProfissional(registroProfissional);
        return endereco.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Buscar endereço por usuário
    @GetMapping("/usuario/{cpfUser}")
    public ResponseEntity<Endereco> buscarEnderecoPorUsuario(@PathVariable String cpfUser) {
        Optional<Endereco> endereco = enderecoService.buscarEnderecoPorUsuario(cpfUser);
        return endereco.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Listar todos os endereços
    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos() {
        List<Endereco> enderecos = enderecoService.listarTodos();
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(enderecos);
        }
    }
}
