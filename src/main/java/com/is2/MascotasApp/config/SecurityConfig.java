package com.is2.MascotasApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.is2.MascotasApp.services.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(usuarioService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http
	        .csrf(csrf -> csrf.disable())  
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/usuario/**").authenticated()
	            .requestMatchers("/public/**").permitAll()
	            // Permito cualquier otra ruta
	            .anyRequest().permitAll())
	        .formLogin(formLogin -> formLogin
	            .loginPage("/login")  // Pag de login personalizada
	            .loginProcessingUrl("/loginCheck")  
	            .usernameParameter("email")  
	            .passwordParameter("password")  
	            .defaultSuccessUrl("/usuario", true)  // Redireccion cuando se logeo 
	            .permitAll())
	        .logout(logout -> logout
	            .logoutUrl("/logout")  // URL para cerrar sesión
	            .logoutSuccessUrl("/usuario/logout")  // Redireccion cuando se deslogeo 
	            .permitAll())
	        .build();
	}


}
