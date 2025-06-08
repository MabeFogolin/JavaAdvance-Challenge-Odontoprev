package com.fiap.N.I.B.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "imagens")
public class Imagem {

    @Id
    private String id;
    private String nome;
    private String contentType;
    private byte[] dados;
    private int verificado;

}
