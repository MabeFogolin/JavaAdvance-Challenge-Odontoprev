package com.fiap.N.I.B.gateways.Imagem;

import com.fiap.N.I.B.gateways.Repositories.ImagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PaginaUploadController {

    @Autowired
    ImagemRepository imagemRepository;

    @GetMapping("/upload-imagem")
    public String mostrarFormularioUpload() {
        return "upload";
    }

//    @GetMapping("/imagens/listar")
//    public String listarImagens(Model model) {
//        model.addAttribute("imagens", imagemRepository.findAll());
//        return "listar-imagens";
//    }
//
//    @GetMapping("/imagens/{id}")
//    public ResponseEntity<byte[]> getImagem(@PathVariable String id) {
//        return imagemRepository.findById(id)
//                .map(imagem -> ResponseEntity.ok()
//                        .header("Content-Type", imagem.getContentType())
//                        .body(imagem.getDados()))
//                .orElse(ResponseEntity.notFound().build());
//    }
}
