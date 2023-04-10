package com.sip.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.sip.entities.Article;
import com.sip.repositories.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sip.entities.Provider;
import com.sip.repositories.ProviderRepository;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/provider")
public class ProviderController {

    public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads/logoProviders";
    public String trouve=null;
private final ProviderRepository providerRepository;
    private final ArticleRepository articleRepository;
	

    @Autowired
    public ProviderController(ProviderRepository providerRepository,ArticleRepository articleRepository) {
        this.providerRepository = providerRepository;
        this.articleRepository = articleRepository;
    }


    @GetMapping("list")
    public String listProviders(Model model) {
    	
    	List<Provider> lp = (List<Provider>)providerRepository.findAll();
    	if(lp.size() == 0)
    		lp = null;
        model.addAttribute("chercher","provider");
        model.addAttribute("providers", lp);
        model.addAttribute("trouveProvider", trouve);
        trouve =null;
        return "provider/listProviders";

    }
    
    @GetMapping("add")
    public String showAddProviderForm(Model model) {
    	Provider provider = new Provider();// object dont la valeur des attributs par defaut
    	model.addAttribute("provider", provider);
        model.addAttribute("chercher","provider");
        return "provider/addProvider";
    }
    
    @PostMapping("add")
    public String addProvider(@Valid Provider provider,@RequestParam("files") MultipartFile[] files, BindingResult result) {
        if (result.hasErrors()) {
            return "provider/addProvider";
        }
        if(provider.getName()=="")
        	provider.setName(null);

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
        provider.setLogo(fileName.toString());
       // provider.setLogo(fileId);
        providerRepository.save(provider);
        return "redirect:list";
    }

    
    @GetMapping("delete/{id}")
    public String deleteProvider(@PathVariable("id") long id, Model model) {

    	//long id2 = 100L;
    	
        /*Provider provider = providerRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("Invalid provider Id:" + id));*/
        Optional<Provider>provider = providerRepository.findById(id);

        if(provider.isPresent())
        {
            providerRepository.delete(provider.get());
            trouve="existe";
        }
        else {
            trouve="inexiste";
        }
        model.addAttribute("trouveProvider", trouve);

        return "redirect:../list";
    }
    
    
    @GetMapping("edit/{id}")
    public String showProviderFormToUpdate(@PathVariable("id") long id, Model model) {
        Provider provider = providerRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + id));
        
        model.addAttribute("provider", provider);
        model.addAttribute("chercher","provider");
        return "provider/updateProvider";
    }
    @PostMapping("update")
    public String updateProvider(@Valid Provider provider,@RequestParam("files") MultipartFile[] files, BindingResult result, Model model) {
    	
    	if (result.hasErrors()) {
    		/* Provider providerToUpdate = providerRepository.findById(provider.getId())
    		            .orElseThrow(()->new IllegalArgumentException("Invalid provider Id:" + provider.getId()));
    		        
    		        model.addAttribute("provider", providerToUpdate);*/
            return "provider/updateProvider";
        }

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
        provider.setLogo(fileName.toString());
        // provider.setLogo(fileId);
    	
    	providerRepository.save(provider);
    	return"redirect:list";
    }

    @GetMapping("show/{id}")
    public String showProvider(@PathVariable("id") long id, Model model) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid provider Id:" + id));
        List<Article> articles = articleRepository.findArticlesByProvider(id);
        /*for (Article a : articles)
            System.out.println("Article = " + a.getLabel());*/
        model.addAttribute("chercher","provider");
        model.addAttribute("articles", articles);
        model.addAttribute("provider", provider);
        return "provider/showProvider";
    }
    @GetMapping("chercher")
    public String chercherProviders(@RequestParam("search") String mots,Model model) {

        List<Provider> lp = (List<Provider>)providerRepository.findProviders(mots);
        if(lp.size() == 0)
            lp = null;
        model.addAttribute("chercher","provider");
        model.addAttribute("providers", lp);
        model.addAttribute("trouveProvider", trouve);
        trouve =null;
        return "provider/listProviders";

    }

}
