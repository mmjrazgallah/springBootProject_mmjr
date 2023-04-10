package com.sip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sip.entities.Provider;
import com.sip.repositories.ProviderRepository;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;

@SpringBootApplication
public class CampSpringApplication extends SpringBootServletInitializer implements CommandLineRunner {
	@Autowired
	private JavaMailSender javaMailSender;

	void sendEmail() {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("mmjr27@gmail.com");

		msg.setSubject("Testing from Spring Boot");
		msg.setText("Hello World \n Spring Boot Email");

		javaMailSender.send(msg);

	}

	@Override
	public void run(String... args) throws MessagingException, IOException {

		//System.out.println("Sending Email...");
		//sendEmail();
		//System.out.println("Done");

	}

	public static void main(String[] args) {
		SpringApplication.run(CampSpringApplication.class, args);

		//BCryptPasswordEncoder b= new BCryptPasswordEncoder();
		//System.out.println(b.encode("123456"));
		//System.out.println("Couche Data");
		//Provider p1 = new Provider();
	   // Provider p2 = new Provider();
		
		 	
	}

}
