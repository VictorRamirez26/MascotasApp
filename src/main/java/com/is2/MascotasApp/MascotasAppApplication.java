package com.is2.MascotasApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.is2.MascotasApp.services.UsuarioService;

@SpringBootApplication
public class MascotasAppApplication {

	@Autowired UsuarioService usuarioService;
	public static void main(String[] args) {
		SpringApplication.run(MascotasAppApplication.class, args);
	}

	
}
