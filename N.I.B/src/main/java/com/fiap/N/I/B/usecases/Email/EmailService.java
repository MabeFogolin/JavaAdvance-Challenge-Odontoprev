package com.fiap.N.I.B.usecases.Email;

import org.thymeleaf.context.Context;

public interface EmailService {
    void sendEmailWithHtml(String to, String subject, String template, Context context);
}