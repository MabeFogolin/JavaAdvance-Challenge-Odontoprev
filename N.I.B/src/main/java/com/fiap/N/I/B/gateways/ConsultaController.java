package com.fiap.N.I.B.gateways;

import com.fiap.N.I.B.domains.Consulta;
import com.fiap.N.I.B.gateways.requests.ConsultaPatch;
import com.fiap.N.I.B.gateways.responses.ConsultaPostResponse;
import com.fiap.N.I.B.usecases.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    // Criar nova consulta
    @PostMapping("/criar/{cpfUser}/{registroProfissional}")
    public ResponseEntity<ConsultaPostResponse> criarConsulta(@PathVariable String cpfUser,
                                                              @PathVariable String registroProfissional,
                                                              @RequestBody Consulta consultaParaInserir) {
        ConsultaPostResponse respostaCriacao = consultaService.criarConsulta(cpfUser, registroProfissional, consultaParaInserir);
        if (respostaCriacao.getMensagem().equals("Nova consulta adicionada")) {
            return ResponseEntity.status(201).body(respostaCriacao);
        } else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
    }

    // Buscar consultas por usuário
    @GetMapping("/usuario/{cpfUser}")
    public ResponseEntity<List<Consulta>> buscarConsultasPorUsuario(@PathVariable String cpfUser) {
        List<Consulta> consultas = consultaService.consultasPorUsuario(cpfUser);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }

    // Buscar consultas por profissional
    @GetMapping("/profissional/{registroProfissional}")
    public ResponseEntity<List<Consulta>> buscarConsultasPorProfissional(@PathVariable String registroProfissional) {
        List<Consulta> consultas = consultaService.consultasPorProfissional(registroProfissional);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }

    // Atualizar uma consulta (PUT)
    @PutMapping("/atualizar/{cpfUser}/{dataConsulta}")
    public ResponseEntity<Consulta> atualizarConsulta(@PathVariable String cpfUser,
                                                      @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataConsulta,
                                                      @RequestBody Consulta consultaParaAtualizar) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarConsultaTotalmente(cpfUser, dataConsulta, consultaParaAtualizar);
        return consultaAtualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar informações específicas de uma consulta (PATCH)
    @PatchMapping("/atualizar-informacoes/{cpfUser}/{dataConsulta}")
    public ResponseEntity<Consulta> atualizarInformacoesConsulta(@PathVariable String cpfUser,
                                                                 @PathVariable String registroProfissional,
                                                                 @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataConsulta,
                                                                 @RequestBody ConsultaPatch consultaPatch) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarInformacoesConsulta(cpfUser, registroProfissional, dataConsulta, consultaPatch);
        return consultaAtualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar uma consulta
    @DeleteMapping("/deletar/{cpfUser}/{dataConsulta}")
    public ResponseEntity<Void> deletarConsulta(@PathVariable String cpfUser,
                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataConsulta) {
        boolean deletado = consultaService.deletarRegistro(cpfUser, dataConsulta);
        if (deletado) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // Buscar todos os registros de consultas
    @GetMapping("/todos")
    public ResponseEntity<List<Consulta>> buscarTodasConsultas() {
        List<Consulta> consultas = consultaService.todosRegistros();
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }
}
