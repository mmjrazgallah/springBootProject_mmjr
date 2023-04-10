package com.sip.controllers;

import com.sip.entities.Article;
import com.sip.entities.Provider;
import com.sip.entities.User;
import com.sip.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("show/{email}")
    public String showProfile(@PathVariable("email") String email, Model model) {
        User user = userService.findUserByEmail(email);
        model.addAttribute("userConnect", user);
        return "/profile";
    }
}
