package com.is2.MascotasApp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void crear(String nombre, String apellido, String email, String clave) throws ErrorServiceException{
		
		validar(nombre,apellido,email,clave);
		
		Usuario usuario = Usuario.builder().nombre(nombre).apellido(apellido)
				.email(email).clave(clave).alta(new Date()).build();
		
		usuarioRepository.save(usuario);
		
	}
	
	public void modificar(String id, String nombre, String apellido, String email, String clave) throws ErrorServiceException{
		
		Optional<Usuario> respuesta = usuarioRepository.findById(id);
		
		if (respuesta.isPresent()) {
			
			validar(nombre, apellido, email, clave);
			Usuario usuario = respuesta.get();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setClave(clave);
			usuarioRepository.save(usuario);
			
		}else {
			throw new ErrorServiceException("Usuario no encontrado");
		}
		
	}
	
	public void eliminar(String id) throws ErrorServiceException {
		Optional<Usuario> respuesta = usuarioRepository.findById(id);
		
		if (respuesta.isPresent()) {
			
			Usuario usuario = respuesta.get();
			usuario.setBaja(new Date());
			usuarioRepository.save(usuario);
			
		}else {
			throw new ErrorServiceException("Usuario no encontrado");
		}
	}
	
	private void validar(String nombre, String apellido, String email, String clave) throws ErrorServiceException{
		
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServiceException("Ingrese el nombre");
		}
		
		if (apellido == null || apellido.isEmpty()) {
			throw new ErrorServiceException("Ingrese el apellido");
		}
		
		if (email == null || nombre.isEmpty()) {
			throw new ErrorServiceException("Ingrese el email");
		}
		
		if (clave == null || clave.isEmpty()) {
			throw new ErrorServiceException("Ingrese la clave");
		}
		
		if (clave.length() <= 6) {
			throw new ErrorServiceException("Su clave debe tener más de 6 dígitos");
		}
		
	}
	
	
}
