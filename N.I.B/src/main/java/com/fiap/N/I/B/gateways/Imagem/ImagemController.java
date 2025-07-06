package com.fiap.N.I.B.gateways.Imagem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.N.I.B.domains.Imagem;
import com.fiap.N.I.B.gateways.Repositories.ImagemRepository;
import com.fiap.N.I.B.gateways.requests.ImagemUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    @Autowired
    private ImagemRepository imagemRepository;

    @PostMapping("/upload-url")
    public ResponseEntity<String> uploadImagemPorUrl(@RequestBody ImagemUrlRequest request) {
        try {
            URL url = new URL(request.getUrl());
            URLConnection connection = url.openConnection();
            String contentType = connection.getContentType();

            try (InputStream inputStream = connection.getInputStream()) {
                byte[] dados = inputStream.readAllBytes();

                Imagem imagem = new Imagem();
                imagem.setNome(request.getNome());
                imagem.setContentType(contentType);
                imagem.setDados(dados);

                imagemRepository.save(imagem);
                return ResponseEntity.ok("Imagem salva com sucesso!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao baixar ou salvar a imagem: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagemDireta(@RequestParam("imagem") MultipartFile file) {
        System.out.println("Arquivo recebido: " + file.getOriginalFilename());
        try {
            String nome = file.getOriginalFilename();
            if (nome == null || nome.isBlank()) {
                nome = "imagem_padrao.jpg";
            }

            byte[] dados = file.getBytes();
            String imagemBase64 = Base64.getEncoder().encodeToString(dados);

            Imagem imagem = new Imagem();
            imagem.setNome(nome);
            imagem.setContentType(file.getContentType());
            imagem.setDados(dados);
            imagem.setVerificado(0);
            imagemRepository.save(imagem);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInput = objectMapper.writeValueAsString(Map.of("image_base64", imagemBase64));

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:6000/detect"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode responseJson = objectMapper.readTree(response.body());
                int verificado = responseJson.has("verificado") ? responseJson.get("verificado").asInt() : 0;

                imagem.setVerificado(verificado);
                imagemRepository.save(imagem);

                if (verificado == 0){
                    return ResponseEntity.ok("Imagem não válida: " + verificado);
                }

                return ResponseEntity.ok("Imagem enviada e analisada com sucesso. Status verificado: " + verificado);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao processar imagem no Flask: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao enviar imagem para Flask: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao processar upload.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImagem(@PathVariable String id) {
        return imagemRepository.findById(id)
                .map(img -> ResponseEntity.ok()
                        .header("Content-Type", img.getContentType())
                        .body(img.getDados()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/json")
    public ResponseEntity<?> getImagemComoJson(@PathVariable String id) {
        return imagemRepository.findById(id)
                .map(img -> {
                    String base64 = Base64.getEncoder().encodeToString(img.getDados());

                    Map<String, Object> response = new HashMap<>();
                    response.put("id", img.getId());
                    response.put("nome", img.getNome());
                    response.put("contentType", img.getContentType());
                    response.put("dados", base64);

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Map<String, Object>>> listarNomesEIds() {
        List<Map<String, Object>> imagensListadas = imagemRepository.findAll().stream().map(imagem -> {
            Map<String, Object> dadosImagem = new HashMap<>();
            dadosImagem.put("nome", imagem.getNome());
            dadosImagem.put("id", imagem.getId());
            dadosImagem.put("verificado", imagem.getVerificado());
            return dadosImagem;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(imagensListadas);
    }



}