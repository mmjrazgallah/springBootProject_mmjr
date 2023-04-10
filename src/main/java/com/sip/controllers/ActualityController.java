package com.sip.controllers;


import com.sip.entities.Actuality;
import com.sip.entities.Article;
import com.sip.entities.Provider;
import com.sip.repositories.ActualityRepository;
import com.sip.repositories.ArticleRepository;
import com.sip.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/actuality/")
public class ActualityController {

    private final ActualityRepository actualityRepository;
    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads/actualityMedia";


    @Autowired
    public ActualityController(ActualityRepository actualityRepository) {
        this.actualityRepository = actualityRepository;

    }

    @GetMapping("add")
    public String showAddActualityForm(Model model) {
        model.addAttribute("chercher","actuality");
        model.addAttribute("actualitys", actualityRepository.findAll());
        model.addAttribute("actuality", new Actuality());
        return "actuality/addActuality";
    }

    @PostMapping("add")
    //@ResponseBody
    public String addActuality(@Valid Actuality actuality, BindingResult result,
                             @RequestParam(name ="filesPic") MultipartFile[] filesPic, @RequestParam(name ="filesMedia") MultipartFile[] filesMedia
    ) {

        /// part upload

        // upload des ficher
        MultipartFile filePic = filesPic[0];
        Path filePicNameAndPath = Paths.get(uploadDirectory, filePic.getOriginalFilename());
        MultipartFile fileMedia = filesMedia[0];
        Path fileMediaNameAndPath = Paths.get(uploadDirectory, fileMedia.getOriginalFilename());

        try {
            Files.write(filePicNameAndPath, filePic.getBytes());
            Files.write(fileMediaNameAndPath, fileMedia.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stockage du name du ficher dans la base
        StringBuilder filePicName = new StringBuilder();
        filePicName.append(filePic.getOriginalFilename());
        actuality.setPicture(filePicName.toString());

        StringBuilder fileMediaName = new StringBuilder();
        fileMediaName.append(fileMedia.getOriginalFilename());
        actuality.setMedia(fileMediaName.toString());


        actualityRepository.save(actuality);
        return "redirect:list";

    }

    @GetMapping("list")
    public String listActualitys(Model model) {
                List<Actuality> la = (List<Actuality>)actualityRepository.findAll();
        if(la.size()==0)
            la = null;
        model.addAttribute("chercher","actuality");
        model.addAttribute("actualitys", la);
       // model.addAttribute("trouve", trouve);
     //   trouve =null;
        return "actuality/listActualitys";
    }
}
