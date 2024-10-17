package com.is2.MascotasApp.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.entities.Zona;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.services.UsuarioService;
import com.is2.MascotasApp.services.ZonaService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ZonaService zonaService;
	
	@PreAuthorize("hashAnyRole('ROL_USUARIO_REGISTRADO')")
	@GetMapping("")
	public String inicio() {
		return "/usuario/inicio.html";
	}
	
	@PreAuthorize("hashAnyRole('ROL_USUARIO_REGISTRADO')")
	@GetMapping("/editar-perfil/{id}")
	public String editarPerfil(@PathVariable("id") String idUsuario, ModelMap model, HttpSession session) {
		
		Usuario login = (Usuario) session.getAttribute("usuarioSession");
		if (login == null || !login.getId().equals(idUsuario)) {
			model.put("error", "Debe registrarse con el usuario correcto para poder acceder");
			return "/public/login.html";
		}
		
		List<Zona> zonas = zonaService.listarZonasActivas();
		model.put("zonas", zonas);
		
		try {
			Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
			model.put("perfil", usuario);
		} catch (Exception e) {
			model.put("error", "Error de sistemas.");
		}
	
		return "/usuario/perfil.html";
	}
	
	@PreAuthorize("hashAnyRole('ROL_USUARIO_REGISTRADO')")
	@PostMapping("/actualizar-perfil")
	public String actualizarPerfil(@RequestParam(value = "id") String idUsuario ,
			@RequestParam(value = "nombre") String nombre,
			@RequestParam(value = "apellido") String apellido,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "clave1") String clave1,
			@RequestParam(value = "clave2") String clave2,
			@RequestParam(value = "archivo") MultipartFile foto,
			@RequestParam(value = "idZona") String idZona,
			ModelMap model, HttpSession session) {
		
		Usuario login = (Usuario) session.getAttribute("usuarioSession");
		if (login == null || !login.getId().equals(idUsuario)) {
			return "/public/login.html";
		}
		
		try {
			usuarioService.modificar(idUsuario, nombre, apellido, email, clave1, clave2, foto, idZona);
			Usuario usuario = usuarioService.buscarUsuarioPorId(idUsuario);
			session.setAttribute("usuarioSession", usuario);
			
		} catch (ErrorServiceException e) {
			model.put("error", e.getMessage());
			List<Zona> zonas = zonaService.listarZonasActivas();
			model.put("zonas", zonas);
			return "/usuario/perfil.html";
			
		}catch (Exception e) {
			model.put("error", "Error de sistemas.");
			return "/usuario/perfil.html";
		}
		
		return "/usuario/inicio.html";
	}
	
	@GetMapping("/logout")
	public String logout(ModelMap model , HttpSession session) {
    	model.put("logout", "Se ha deslogeado correctamente");
    	session.setAttribute("usuarioSession", null);
    	return "/public/login.html";
	}
	
	
}
