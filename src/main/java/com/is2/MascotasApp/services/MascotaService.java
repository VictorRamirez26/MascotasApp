package com.is2.MascotasApp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is2.MascotasApp.entities.Mascota;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.enums.Sexo;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.MascotaRepository;
import com.is2.MascotasApp.repositories.UsuarioRepository;

@Service
public class MascotaService {

	@Autowired
	private MascotaRepository mascotaRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public void crear(String idUsuario, String nombre, Sexo sexo) throws ErrorServiceException{
		
		Optional<Usuario> respuesta = usuarioRepository.findById(idUsuario);
		
		if (respuesta.isPresent()) {
			Usuario usuario = respuesta.get();
			validar(nombre, sexo);
			Mascota mascota = Mascota.builder().nombre(nombre).sexo(sexo)
					.alta(new Date()).usuario(usuario).build();
			mascotaRepository.save(mascota);
		} else {
			throw new ErrorServiceException("Debe seleccionar el usuario");
		}
		
	}
	
	public void modificar(String idMascota, String idUsuario, String nombre, Sexo sexo) throws ErrorServiceException {
		
		Optional<Mascota> respuestaMascota = mascotaRepository.findById(idMascota);
		if (respuestaMascota.isPresent()) {
			Mascota mascota = respuestaMascota.get();
			validar(nombre, sexo);
			
			if (validarMascotaUsuario(mascota,idUsuario)) {
				mascota.setNombre(nombre);
				mascota.setSexo(sexo);
				mascotaRepository.save(mascota);		
			}else {
				throw new ErrorServiceException("No tiene permisos para modificar esta mascota");
			}
						
		} else {
			throw new ErrorServiceException("Debe seleccionar una mascota");
		}
		
	}
	
	public void eliminar(String idMascota, String idUsuario) throws ErrorServiceException {
		
		Optional<Mascota> respuesta = mascotaRepository.findById(idMascota);
		if (respuesta.isPresent()) {
			Mascota mascota = respuesta.get();
			if (validarMascotaUsuario(mascota,idUsuario)) {				
				mascota.setBaja(new Date());
				mascotaRepository.save(mascota);
			} else {
				throw new ErrorServiceException("No tiene permisos para eliminar esta mascota");
			}
			
		} else {
			throw new ErrorServiceException("Debe seleccionar una mascota");
		}
	}
	
	// Verifico que el que realice la modificación o eliminación sea el dueño de la mascota
	private boolean validarMascotaUsuario(Mascota mascota, String idUsuario) {
		
		if (mascota.getUsuario().getId().equals(idUsuario)) {
			return true;
		}
		return false;
	}
	
	private void validar(String nombre, Sexo sexo) throws ErrorServiceException {
		
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServiceException("Ingrese el nombre de la mascota");
		}
		
		if (sexo == null) {
			throw new ErrorServiceException("Ingrese el sexo de la mascota");
		}
	}
}
