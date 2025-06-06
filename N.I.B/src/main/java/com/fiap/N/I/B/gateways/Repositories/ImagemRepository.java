package com.fiap.N.I.B.gateways.Repositories;

import com.fiap.N.I.B.domains.Imagem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImagemRepository extends MongoRepository<Imagem, String> {
}
