package com.fiap.N.I.B.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String to;
    private String subject;
    private String message;
    private String name;
}