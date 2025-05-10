package com.fiap.N.I.B.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if( userDetails != null ) {
            model.addAttribute("username", userDetails.getUsername());
        }
        return "home";
    }

    @GetMapping("/localization")
    public String showLocalizationExample(Model model) {
        model.addAttribute("price", 1234.56);
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("userName", "John");
        return "localization";
    }

    @GetMapping("access-denied")
    public String accessDenied() {
        return "access-denied";
    }

}
