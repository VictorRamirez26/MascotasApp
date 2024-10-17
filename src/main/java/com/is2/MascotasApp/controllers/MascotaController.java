package com.is2.MascotasApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Mascota;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.enums.Sexo;
import com.is2.MascotasApp.enums.Tipo;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.services.MascotaService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/mascota")
@PreAuthorize("hashAnyRole('ROL_USUARIO_REGISTRADO')")
public class MascotaController {

	@Autowired
	private MascotaService mascotaService;
	
	@GetMapping("/mis-mascotas")
	public String listarMascotas(ModelMap model , HttpSession session) {
		
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		if (usuario == null) {
			model.put("error", "Debe registrarse para poder acceder");
			return "/public/login.html";
		}
		
		List<Mascota> mascotas = mascotaService.listarMascotasActivasPorUsuario(usuario.getId());
		model.put("mascotas", mascotas);
		
		return "/usuario/mascotas.html";
	}
	
	@GetMapping("/editar-perfil-mascota")
	public String editarMascota(@RequestParam(value = "id" , required = false) String idMascota ,
			@RequestParam(value = "accion") String accion , 
			ModelMap model , HttpSession session) {
		
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
		if (usuario == null) {
			model.put("error", "Debe registrarse para poder acceder");
			return "/public/login.html";
		}
		
		Mascota mascota;
		if (idMascota == null || idMascota.isEmpty()) {			
			mascota = new Mascota();
		}else {
			mascota = mascotaService.buscarMascotaPorId(idMascota);
			
			Usuario login = (Usuario) session.getAttribute("usuarioSession");
			if (!login.getId().equals(mascota.getUsuario().getId())) {
				session.setAttribute("usuarioSession", null);
				model.put("error", "No puede modificar esta mascota.");
				return "/public/login.html";
			}
		}
		
		
		model.put("mascota", mascota);
		model.put("accion", accion);
		model.put("tipos", Tipo.values());
		model.put("sexos", Sexo.values());
		return "/usuario/mascota.html";
	}
	
	@PostMapping("/actualizar-perfil-mascota")
	public String actualizarMascota (@RequestParam(value = "id") String idMascota,
			@RequestParam(value = "nombre") String nombre,
			@RequestParam(value = "tipo") Tipo tipo,
			@RequestParam(value = "sexo") Sexo sexo,
			@RequestParam(value = "archivo")  MultipartFile foto,
			ModelMap model, HttpSession session) {
		
		Usuario usuario = (Usuario) session.getAttribute("usuarioSession");

		try {
			
			if (idMascota == null || idMascota.isEmpty()) {
				mascotaService.crear(usuario.getId(), nombre, sexo, tipo, foto);
			}else {
				mascotaService.modificar(idMascota , usuario.getId(), nombre, sexo, tipo, foto);				
			}
			
			List<Mascota> mascotas = mascotaService.listarMascotasActivasPorUsuario(usuario.getId());
			model.put("mascotas", mascotas);
			
			return "/usuario/mascotas.html";
			
		} catch (ErrorServiceException e) {
			model.put("error", e.getMessage());
		}catch (Exception e) {
			model.put("error", "Error de sistema");
		}
		
		return "/usuario/mascota.html";
	}
	
	@PostMapping("/eliminar-perfil-mascota")
	public String eliminarMascota(@RequestParam(value = "id") String idMascota , HttpSession session, ModelMap model) {
		
		try {
			
			Usuario usuario = (Usuario) session.getAttribute("usuarioSession");
			mascotaService.eliminar(idMascota, usuario.getId());
			return "redirect:/mascota/mis-mascotas";
		} catch (ErrorServiceException e) {
			model.put("error",e.getMessage());
		}catch (Exception e) {
			model.put("error", "Error de sistema");
		}
		
		return "/usuario/mascota.html";
	}
	
	
}
