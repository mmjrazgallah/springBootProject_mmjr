package com.sip.controllers;

import com.sip.entities.Actuality;
import com.sip.entities.User;
import com.sip.repositories.ActualityRepository;
import com.sip.repositories.ArticleRepository;
import com.sip.repositories.ProviderRepository;
import com.sip.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;
    private final ProviderRepository providerRepository;
    private final ArticleRepository articleRepository;

    private final ActualityRepository actualityRepository;
    @Autowired
    private JavaMailSender javaMailSender;



    public HomeController(UserService userService, ProviderRepository providerRepository, ArticleRepository articleRepository, ActualityRepository actualityRepository) {
        this.userService = userService;
        this.providerRepository=providerRepository;
        this.articleRepository=articleRepository;
        this.actualityRepository=actualityRepository;
    }

    @RequestMapping(value={"/","h**"})
    //@ResponseBody
    public String home(Model model) {
        long nbrUsers=userService.nbrUsers();
        long nbrUsersDisables=userService.nbrUsersDisables();
        long nbrProviders=providerRepository.count();
        long nbrArticles=articleRepository.count();
        List<Actuality> la = (List<Actuality>)actualityRepository.findAll();
        if(la.size()==0)
            la = null;
        model.addAttribute("actualitysHome", la);
        model.addAttribute("nbrUsers",nbrUsers);
        model.addAttribute("nbrUsersDisables",nbrUsersDisables);
        model.addAttribute("nbrProviders",nbrProviders);
        model.addAttribute("nbrArticles",nbrArticles);
        return "/index";
    }

    @RequestMapping(value={"/login"})
    public String login() {
        return "/login";
    }

    @RequestMapping(value={"/logout"})
    public String logout() {
        return "/login";
    }

    @RequestMapping(value={"/403"})
    public String error403() {
        return "/error/403";
    }

  /* @PostMapping("/login")
    public String Verifierlogin(@RequestParam(name = "email", required = true) String email, @RequestParam(name = "password", required = true) String password, Model model) {
       User userConnect = userService.verifierUser(email,password);
       System.out.println(userConnect.getName());
        if (userConnect !=null)
       model.addAttribute("userConnect", userConnect);
        return "/login";
    }

       @PostMapping("/login")
    public String Verifierlogin(@RequestParam(name = "email", required = true) String email, @RequestParam(name = "password", required = true) String password, Model model) {
       User userConnect = userService.findUserByEmail(email);
       model.addAttribute("userConnect", userConnect);
        return "/login";
    }*/

    public String registration() {
        return "";
    }

    public String forgotpassword() {
        return "";
    }


   @GetMapping("/contact")
   public String contact() {
        return "/contact";
    }

    @PostMapping("/contact")
    public String contactSubmit(@RequestParam(name = "username", required = true) String username,@RequestParam(name = "email", required = true) String email,@RequestParam(name = "phone", required = true) String phone,@RequestParam(name = "suggestions", required = true) String suggestions,Model model) {
        String emailSuperAdmin="mmjrformation@gmail.com";
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailSuperAdmin);

        msg.setSubject("contact From "+username);
        msg.setText("Hello Super Admin, veuillez contacter  "+username+" by email "+email+"or By phone"+phone
                + " \n Subject: "+suggestions);
        javaMailSender.send(msg);
        String reponse =new String("success suggestions send ");
        model.addAttribute("reponse",reponse);
        reponse =null;
        return "/contact";
    }

    @RequestMapping("profile/{email}")
    //@ResponseBody
    public String showProfile(@PathVariable ("email") String email,Model model) {

        User user = userService.findUserByEmail(email);
        model.addAttribute("userConnected",user);
        return "/profile";
    }
}
