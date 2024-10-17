package com.is2.MascotasApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.is2.MascotasApp.entities.Mascota;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.services.MascotaService;
import com.is2.MascotasApp.services.UsuarioService;

@Controller
@RequestMapping("/foto")
public class FotoController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MascotaService mascotaService;
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<byte[]> mostrarFoto(@PathVariable String id) {
		
		try {
			Usuario usuario = usuarioService.buscarUsuarioPorId(id);
			
			if (usuario.getFoto() == null) {
				throw new ErrorServiceException("No hay una foto cargada");
			}
			
			byte[] foto = usuario.getFoto().getContenido();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<>(foto,httpHeaders,HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			// TODO: handle exception
		}
		
	}
	
	@GetMapping("/mascota/{id}")
	public ResponseEntity<byte[]> mostrarFotoMascota(@PathVariable String id) {
		
		try {
			Mascota mascota = mascotaService.buscarMascotaPorId(id);
			
			if (mascota.getFoto() == null) {
				throw new ErrorServiceException("No hay una foto cargada");
			}
			
			byte[] foto = mascota.getFoto().getContenido();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<>(foto,httpHeaders,HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			// TODO: handle exception
		}
		
	}
}
