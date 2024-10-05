package com.fiap.N.I.B.gateways.Consulta;

import com.fiap.N.I.B.domains.Consulta;
import com.fiap.N.I.B.gateways.requests.ConsultaPatch;
import com.fiap.N.I.B.gateways.responses.ConsultaPostResponse;
import com.fiap.N.I.B.usecases.Consulta.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    // Criar nova consulta
    @PostMapping("/criar")
    public ResponseEntity<ConsultaPostResponse> criarConsulta(@RequestParam String cpfUser,
                                                              @RequestParam String registroProfissional,
                                                              @RequestBody Consulta consultaParaInserir) {
        ConsultaPostResponse respostaCriacao = consultaService.criarConsulta(cpfUser, registroProfissional, consultaParaInserir);
        if (respostaCriacao.getMensagem().equals("Nova consulta adicionada")) {
            return ResponseEntity.status(201).body(respostaCriacao);
        } else {
            return ResponseEntity.status(409).body(respostaCriacao);
        }
    }

    // Buscar consultas por usuário
    @GetMapping("/usuario")
    public ResponseEntity<List<Consulta>> buscarConsultasPorUsuario(@RequestParam String cpfUser) {
        List<Consulta> consultas = consultaService.consultasPorUsuario(cpfUser);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }

    // Buscar consultas por profissional
    @GetMapping("/profissional")
    public ResponseEntity<List<Consulta>> buscarConsultasPorProfissional(@RequestParam String registroProfissional) {
        List<Consulta> consultas = consultaService.consultasPorProfissional(registroProfissional);
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }

    // Atualizar uma consulta (PUT) com data como query param
    @PutMapping("/atualizar")
    public ResponseEntity<Consulta> atualizarConsulta(@RequestParam String cpfUser,
                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta,
                                                      @RequestBody Consulta consultaParaAtualizar) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarConsultaTotalmente(cpfUser, dataConsulta, consultaParaAtualizar);
        return consultaAtualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Atualizar informações específicas de uma consulta (PATCH) com data como query param
    @PatchMapping("/atualizar-informacoes")
    public ResponseEntity<Consulta> atualizarInformacoesConsulta(@RequestParam String cpfUser,
                                                                 @RequestParam String registroProfissional,
                                                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta,
                                                                 @RequestBody ConsultaPatch consultaPatch) {
        Optional<Consulta> consultaAtualizada = consultaService.atualizarInformacoesConsulta(cpfUser, registroProfissional, dataConsulta, consultaPatch);
        return consultaAtualizada.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Deletar uma consulta com data como query param
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
    public ResponseEntity<List<Consulta>> buscarTodasConsultas() {
        List<Consulta> consultas = consultaService.todosRegistros();
        if (consultas.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(consultas);
        }
    }

    @GetMapping("/usuario/data")
    public ResponseEntity<Consulta> buscarConsultaPorData(@RequestParam String cpfUser,
                                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataConsulta) {
        Optional<Consulta> consulta = consultaService.buscarConsultaPorData(cpfUser, dataConsulta);
        return consulta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
