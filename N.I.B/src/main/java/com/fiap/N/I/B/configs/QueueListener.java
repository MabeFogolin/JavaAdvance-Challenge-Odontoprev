package com.fiap.N.I.B.configs;

import com.fiap.N.I.B.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.Repositories.UsuarioRepository;
import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.model.Profissional;
import com.fiap.N.I.B.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class QueueListener {

    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final ProfissionalRepository profissionalRepository;

    @RabbitListener(queues = "fila-usuario")
    public void usuarioReceiveMessage(String message) {
        try {
            Usuario usuario = objectMapper.readValue(message, Usuario.class);
            usuarioRepository.save(usuario);
            System.out.println("✅ Usuário atualizado no banco de dados com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar a mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = "fila-profissional")
    public void profissionalReceiveMessage(String message) {
        try {
            Profissional profissional = objectMapper.readValue(message, Profissional.class);
            profissionalRepository.save(profissional);
            System.out.println("✅ Profissional atualizado no banco de dados com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao processar a mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

