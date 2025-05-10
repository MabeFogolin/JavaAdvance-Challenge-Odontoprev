package com.fiap.N.I.B.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error",required = false)String error,
                            @RequestParam(value = "logout",required = false)String logout,
                            Model model) {
        if(error !=null) {
            model.addAttribute("error","Usuário ou senha incorretos");
        }
        if(logout != null) {
            model.addAttribute("logout","Deslogado com sucesso");
        }
        return "/login";
    }
}
