package com.is2.MascotasApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Zona;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.services.UsuarioService;
import com.is2.MascotasApp.services.ZonaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class InicioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ZonaService zonaService;
	
	@GetMapping("")
	public String index() {
		return "/public/index.html";
	} 

	@GetMapping("/registro")
	public String registro(ModelMap model) {
		List<Zona> zonas = zonaService.listarZonasActivas();
		model.put("zonas", zonas);
		return "/public/registro.html";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, ModelMap model) {
		if (error != null) {
			model.put("error", "Email y/o contraseña incorrectas");
		}
		return "/public/login.html";
	}
	
	
	@PostMapping("/registro")
	public String registrar(@RequestParam(value = "nombre") String nombre,
			@RequestParam(value = "apellido") String apellido,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "clave1") String clave1,
			@RequestParam(value = "clave2") String clave2,
			@RequestParam(value = "archivo") MultipartFile foto,
			@RequestParam(value = "idZona") String idZona,
			ModelMap model) throws ErrorServiceException , Exception {
		
		try {
			usuarioService.crear(nombre, apellido, email, clave1, clave2 , foto, idZona);
			return "/public/exito.html";
		}catch(ErrorServiceException e) {
			model.put("error", e.getMessage());
			model.put("nombre", nombre);
			model.put("apellido", apellido);
			model.put("email", email);
			List<Zona> zonas = zonaService.listarZonasActivas();
			model.put("zonas", zonas);
			model.put("foto", foto);
			model.put("clave1", clave1);
			model.put("clave2", clave2);
			
			System.out.println("Ocurrio un error");
		}catch (Exception ex) {
			System.out.println("Error de sistema");
		}
		
		return "/public/registro.html";
	}
	
	
}
