package com.fiap.N.I.B.gateways.Email;

import com.fiap.N.I.B.domains.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    /**
     * Envia o e-mail "Não esqueça de preencher seu histórico médico"
     * com base nas informações do usuário.
     */
    @PostMapping("/sendHistorico")
    public String sendHistoricoEmail(@RequestBody Email emailDetails) {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetails.getName());
            context.setVariable("link", "http://localhost:8080/historico/preencher"); // Ajuste conforme seu projeto

            emailServiceImpl.sendEmailWithHtml(
                    emailDetails.getTo(),
                    "Não esqueça de preencher seu histórico médico!",
                    "EmailTemplateHistorico",
                    context
            );

            return "E-mail de histórico médico enviado com sucesso para " + emailDetails.getTo();
        } catch (Exception e) {
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }

    @PostMapping("/sendMelhorarNota")
    public String sendMelhorarNota(@RequestBody Email emailDetails) {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetails.getName());

            emailServiceImpl.sendEmailWithHtml(
                    emailDetails.getTo(),
                    "Vamos melhorar sua nota?",
                    "EmailTemplateMelhorarNota",
                    context
            );
            return "E-mail de incentivo enviado com sucesso para " + emailDetails.getTo();
        } catch (Exception e) {
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }

}
