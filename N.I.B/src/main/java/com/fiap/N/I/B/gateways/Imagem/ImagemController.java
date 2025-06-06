package com.fiap.N.I.B.gateways.Imagem;

import com.fiap.N.I.B.domains.Imagem;
import com.fiap.N.I.B.gateways.Repositories.ImagemRepository;
import com.fiap.N.I.B.gateways.requests.ImagemUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    @Autowired
    private ImagemRepository imagemRepository;

    // Upload via URL (mantido)
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

    // Novo: upload direto via POST multipart/form-data
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagemDireta(@RequestParam("nome") String nome,
                                                     @RequestParam("imagem") MultipartFile imagemArquivo) {
        try {
            byte[] dados = imagemArquivo.getBytes();

            Imagem imagem = new Imagem();
            imagem.setNome(nome);
            imagem.setContentType(imagemArquivo.getContentType());
            imagem.setDados(dados);

            imagemRepository.save(imagem);
            return ResponseEntity.ok("Imagem enviada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao salvar imagem: " + e.getMessage());
        }
    }

   // Novo: exibir imagem pelo ID
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
    public ResponseEntity<?> listarNomesEIds() {
        var imagens = imagemRepository.findAll();

        // Cria uma lista de mapas com id e nome
        var listaSimples = imagens.stream()
                .map(img -> Map.of("id", img.getId(), "nome", img.getNome()))
                .toList();

        return ResponseEntity.ok(listaSimples);
    }


}