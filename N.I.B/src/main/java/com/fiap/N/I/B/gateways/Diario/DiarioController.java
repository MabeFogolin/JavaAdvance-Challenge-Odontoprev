package com.fiap.N.I.B.gateways.Diario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.N.I.B.domains.Diario;
import com.fiap.N.I.B.domains.Imagem;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.Repositories.ConsultaRepository;
import com.fiap.N.I.B.gateways.Repositories.DiarioRepository;
import com.fiap.N.I.B.gateways.Repositories.ImagemRepository;
import com.fiap.N.I.B.gateways.Repositories.UsuarioRepository;
import com.fiap.N.I.B.gateways.requests.DiarioPatch;
import com.fiap.N.I.B.gateways.responses.DiarioPostResponse;
import com.fiap.N.I.B.gateways.responses.DiarioResponse;
import com.fiap.N.I.B.usecases.Diario.DiarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/diario")
@RequiredArgsConstructor
public class DiarioController {

    private final DiarioService diarioService;
    private final DiarioRepository diarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final ImagemRepository imagemRepository;

//    // Criar novo registro no diário
//    @PostMapping("/criar")
//    public ResponseEntity<EntityModel<DiarioPostResponse>> criarRegistro(
//            @RequestParam String cpfUser,
//            @RequestBody Diario diarioParaCriar) {
//        DiarioPostResponse respostaCriacao = diarioService.inserirNoDiario(cpfUser, diarioParaCriar);
//        if (respostaCriacao.getMensagem().equals("Novo registro adicionado ao diário e pontos incrementados")) {
//            EntityModel<DiarioPostResponse> resource = EntityModel.of(respostaCriacao);
//            resource.add(linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"));
//            return ResponseEntity.status(201).body(resource);
//        } else {
//            return ResponseEntity.status(409).body(EntityModel.of(respostaCriacao));
//        }
//    }

 @PostMapping("/criar")
    public ResponseEntity<String> criarRegistro(@RequestBody Diario diarioRequest, @RequestParam String cpfUser) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCpfUser(cpfUser);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }

            Usuario usuario = usuarioOpt.get();

            List<Diario> registrosDiario = diarioRepository.findByUsuario_CpfUser(cpfUser);
            Diario ultimoRegistro = registrosDiario.stream()
                    .max(Comparator.comparing(Diario::getDataRegistro))
                    .orElse(null);

            boolean houveHigiene = diarioRequest.getEscovacaoDiario() > 0 &&
                    diarioRequest.getUsoFioDiario() > 0 &&
                    diarioRequest.getUsoEnxaguanteDiario() > 0;

            if (houveHigiene && ultimoRegistro != null) {
                long diferencaDias = java.time.temporal.ChronoUnit.DAYS.between(ultimoRegistro.getDataRegistro(), diarioRequest.getDataRegistro());

                if (diferencaDias == 1) {
                        usuario.setSequenciaDias(usuario.getSequenciaDias() + 1);
                    } else {
                        usuario.setSequenciaDias(1);
                    } {
                        usuario.setSequenciaDias(usuario.getSequenciaDias());
                    }
                } else {

                    usuario.setSequenciaDias(1);
                }
            usuario.setPontos(usuario.getPontos() + 1);
            usuarioRepository.save(usuario);


            diarioRequest.setUsuario(usuario);
            diarioRepository.save(diarioRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInput = objectMapper.writeValueAsString(Map.of(
                    "novo_usuario", List.of(
                            List.of(
                                    diarioRequest.getEscovacaoDiario(),
                                    diarioRequest.getUsoFioDiario(),
                                    diarioRequest.getUsoEnxaguanteDiario(),
                                    usuario.getSequenciaDias()
                            )
                    )
            ));

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:5000/predict")) // URL do Flask
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("Resposta do Flask: " + response.body());


                DiarioResponse updatedData = objectMapper.readValue(response.body(), DiarioResponse.class);

                usuario.setNota(updatedData.getScore());
                usuarioRepository.save(usuario);

                return ResponseEntity.ok("Registro no diário criado e nota do usuário atualizada.");
            } else {
                System.err.println("Erro ao receber resposta do Flask: " + response.body());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao processar resposta do Flask.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar ou processar dados: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao processar requisição.");
        }
    }

//    @PostMapping(value = "/criar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> criarRegistroComImagem(@RequestPart("diario") Diario diarioRequest,
//                                                         @RequestPart("imagem") MultipartFile imagemArquivo,
//                                                         @RequestParam String cpfUser) {
//        try {
//            Optional<Usuario> usuarioOpt = usuarioRepository.findByCpfUser(cpfUser);
//            if (usuarioOpt.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
//            }
//
//            Usuario usuario = usuarioOpt.get();
//
//            byte[] dadosImagem = imagemArquivo.getBytes();
//            String imagemBase64 = Base64.getEncoder().encodeToString(dadosImagem);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonInputImagem = objectMapper.writeValueAsString(Map.of("image_base64", imagemBase64));
//
//            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
//
//            HttpRequest requestImagem = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:6000/detect")) // URL do Flask
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonInputImagem))
//                    .build();
//
//            HttpResponse<String> responseImagem = client.send(requestImagem, HttpResponse.BodyHandlers.ofString());
//
//            if (responseImagem.statusCode() != 200) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Erro ao verificar imagem no Flask.");
//            }
//
//            // Criar objeto Imagem com dados e verificação
//            JsonNode responseJson = objectMapper.readTree(responseImagem.body());
//            int verificado = responseJson.has("verificado") ? responseJson.get("verificado").asInt() : 0;
//
//            Imagem imagem = new Imagem();
//            imagem.setNome(imagemArquivo.getOriginalFilename());
//            imagem.setContentType(imagemArquivo.getContentType());
//            imagem.setDados(dadosImagem);
//            imagem.setVerificado(verificado);
//
//            imagemRepository.save(imagem);
//
//            // Regras de pontuação e sequência
//            List<Diario> registros = diarioRepository.findByUsuario_CpfUser(cpfUser);
//            Diario ultimoRegistro = registros.stream()
//                    .max(Comparator.comparing(Diario::getDataRegistro))
//                    .orElse(null);
//
//            boolean houveHigiene = diarioRequest.getEscovacaoDiario() > 0 ;
//
//            if (houveHigiene) {
//                if (ultimoRegistro != null) {
//                    long diferencaDias = ChronoUnit.DAYS.between(ultimoRegistro.getDataRegistro(), diarioRequest.getDataRegistro());
//                    if (diferencaDias == 1) {
//                        usuario.setSequenciaDias(usuario.getSequenciaDias() + 1);
//                    } else {
//                        usuario.setSequenciaDias(1);
//                    } {
//                        usuario.setSequenciaDias(usuario.getSequenciaDias());
//                    }
//                } else {
//
//                    usuario.setSequenciaDias(1);
//                }
//            }
//
//
//            usuario.setPontos(usuario.getPontos() + 1);
//            usuarioRepository.save(usuario);
//
//            diarioRequest.setImagemId(imagem.getId());
//            diarioRequest.setUsuario(usuario);
//            diarioRepository.save(diarioRequest);
//
//            // Previsão com Flask (nota)
//            String jsonInput = objectMapper.writeValueAsString(Map.of(
//                    "novo_usuario", List.of(
//                            List.of(
//                                    diarioRequest.getEscovacaoDiario(),
//                                    diarioRequest.getUsoFioDiario(),
//                                    diarioRequest.getUsoEnxaguanteDiario(),
//                                    usuario.getSequenciaDias()
//                            )
//                    )
//            ));
//
//            HttpRequest requestNota = HttpRequest.newBuilder()
//                    .uri(URI.create("http://localhost:5000/predict"))
//                    .header("Content-Type", "application/json; charset=UTF-8")
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
//                    .build();
//
//            HttpResponse<String> responseNota = client.send(requestNota, HttpResponse.BodyHandlers.ofString());
//
//            if (responseNota.statusCode() == 200) {
//                DiarioResponse updatedData = objectMapper.readValue(responseNota.body(), DiarioResponse.class);
//                usuario.setNota(updatedData.getScore());
//                usuarioRepository.save(usuario);
//                return ResponseEntity.ok("Registro criado com imagem e nota atualizada.");
//            } else {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("Erro ao calcular nota do usuário no Flask.");
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro ao processar imagem ou requisição: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Erro inesperado.");
//        }
//    }



    // Buscar registros do diário por usuário
    @GetMapping("/usuario")
    public ResponseEntity<CollectionModel<EntityModel<Diario>>> buscarRegistrosPorUsuario(@RequestParam String cpfUser) {
        List<Diario> registros = diarioService.buscarRegistrosPorUsuario(cpfUser);
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Diario>> registrosModel = registros.stream()
                    .map(diario -> EntityModel.of(diario,
                            linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withSelfRel(),
                            linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios")
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Diario>> collectionModel = CollectionModel.of(registrosModel);
            collectionModel.add(linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Buscar todos os registros do diário
    @GetMapping("/todos")
    public ResponseEntity<CollectionModel<EntityModel<Diario>>> buscarTodosRegistros() {
        List<Diario> registros = diarioService.buscarTodos();
        if (registros.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<EntityModel<Diario>> registrosModel = registros.stream()
                    .map(diario -> EntityModel.of(diario,
                            linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withSelfRel()
                    ))
                    .collect(Collectors.toList());
            CollectionModel<EntityModel<Diario>> collectionModel = CollectionModel.of(registrosModel);
            collectionModel.add(linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withSelfRel());
            return ResponseEntity.ok(collectionModel);
        }
    }

    // Atualizar um registro do diário
    @PutMapping("/atualizar")
    public ResponseEntity<EntityModel<Diario>> atualizarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody Diario diarioParaAtualizar) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarRegistro(cpfUser, dataRegistro, diarioParaAtualizar);
        if (diarioAtualizado.isPresent()) {
            EntityModel<Diario> resource = EntityModel.of(diarioAtualizado.get(),
                    linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"),
                    linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar informações específicas de um registro (patch)
    @PatchMapping("/atualizar-informacoes")
    public ResponseEntity<EntityModel<Diario>> atualizarInformacoesRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro,
            @RequestBody DiarioPatch diarioPatch) {
        Optional<Diario> diarioAtualizado = diarioService.atualizarInformacoesRegistro(cpfUser, dataRegistro, diarioPatch);
        if (diarioAtualizado.isPresent()) {
            EntityModel<Diario> resource = EntityModel.of(diarioAtualizado.get(),
                    linkTo(methodOn(DiarioController.class).buscarRegistrosPorUsuario(cpfUser)).withRel("usuario-diario"),
                    linkTo(methodOn(DiarioController.class).buscarTodosRegistros()).withRel("todos-diarios"));
            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar um registro do diário
    @DeleteMapping("/deletar")
    public ResponseEntity<Void> deletarRegistro(
            @RequestParam String cpfUser,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataRegistro) {
        boolean deletado = diarioService.deletarRegistro(cpfUser, dataRegistro);
        if (deletado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
