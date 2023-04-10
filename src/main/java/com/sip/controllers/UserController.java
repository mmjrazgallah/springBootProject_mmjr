package com.sip.controllers;

import com.sip.entities.Article;
import com.sip.entities.Provider;
import com.sip.entities.Role;
import com.sip.entities.User;
import com.sip.repositories.ProviderRepository;
import com.sip.repositories.RoleRepository;
import com.sip.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


@Controller
    @RequestMapping("/user")
    public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/images/user";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
        public UserController(UserService userService,RoleRepository roleRepository)
        {
            this.userService = userService;
            this.roleRepository=roleRepository;
        }
       /* @RequestMapping("/add")
        @ResponseBody
        public String addUser()
        {
            User user = new User();
            user.setEmail("user@sip.com");
            user.setPassword("123456");
            user.setName("utilisateur");
            user.setLastName("UTILISATEUR");
            this.userService.saveUser(user);
            return "Added";
        }*/

    @GetMapping("add")
    public String showAddUserForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", user);
        return "user/addUser";
    }

    @PostMapping("add")
    public String addUser(@Valid User user,@RequestParam(name = "role", required = true) String role, @RequestParam("filesPhoto") MultipartFile[] files, BindingResult result) {
        if (result.hasErrors()) {
            return "user/addUser";
        }
        if(user.getEmail()=="")
            user.setEmail(null);

        MultipartFile file = files[0];
        //String fileId=String .valueOf(providerRepository.count()+1)+".png";
        // Path fileNameAndPath = Paths.get(uploadDirectory, fileId);
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder fileName = new StringBuilder();
        fileName.append(file.getOriginalFilename());
        user.setPhoto(fileName.toString());
        Role userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        this.userService.saveUser(user);
        return "redirect:list";
    }
    @GetMapping("list")
    public String listUsers(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
           List<User> lu = (List<User>)userService.listUser();
        if(lu.size()==0)
            lu = null;
        model.addAttribute("users", lu);
        return "user/listUsers";
    }
    @GetMapping("enable/{id}/{email}")
    //@ResponseBody
    public String enableUserAcount(@PathVariable ("id") int id,@PathVariable ("email") String email) {

        sendEmail(email, true);
        User user = userService.userById(id);
        user.setActive(1);
        userService.updateUser(user);
        return "redirect:../../list";
    }

    @GetMapping("disable/{id}/{email}")
    //@ResponseBody
    public String disableUserAcount(@PathVariable ("id") int id,@PathVariable ("email") String email) {

        User user = userService.userById(id);
        sendEmail(email, false);
        user.setActive(0);
        userService.updateUser(user);
        return "redirect:../../list";
    }
    @PostMapping("updateRole")
    //@ResponseBody
    public String UpdateUserRole(@RequestParam ("id") int id,
                                 @RequestParam ("newrole")String newRole
                                 ) {

        User user = userService.userById(id);
        Role userRole = roleRepository.findByRole(newRole);

        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userService.updateUser(user);
        return"redirect:list";
    }

    void sendEmail(String email, boolean state) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        if(state == true)
        {
            msg.setSubject("Account Has Been Activated");
            msg.setText("Hello, Your account has been activated. "
                    +
                    "You can log in : http://127.0.0.1:80/login"
                    + " \n Best Regards!");
        }
        else
        {
            msg.setSubject("Account Has Been disactivated");
            msg.setText("Hello, Your account has been disactivated.");
        }
        javaMailSender.send(msg);

    }


}







