package com.is2.MascotasApp.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is2.MascotasApp.entities.Mascota;
import com.is2.MascotasApp.entities.Voto;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.MascotaRepository;
import com.is2.MascotasApp.repositories.VotoRepository;

@Service
public class VotoService {

	@Autowired
	private MascotaRepository mascotaRepository;
	@Autowired
	private VotoRepository votoRepository;
	
	public void votar(String idUsuario, String idMascota1, String idMascota2) throws ErrorServiceException {
		
		if (idMascota1.equals(idMascota2)) {
			throw new ErrorServiceException("No se puede votar a si mismo");
		}
		
		Voto voto = Voto.builder().fecha(new Date()).build();
		
		Optional<Mascota> respuesta1 = mascotaRepository.findById(idMascota1);
		
		if (respuesta1.isPresent()) {
			Mascota mascota1 = respuesta1.get();
			
			if (mascota1.getUsuario().getId().equals(idUsuario)) {
				voto.setMascota1(mascota1);
			} else {
				throw new ErrorServiceException("No tiene permisos para realizar esta operación.");
			}
		} else {
			throw new ErrorServiceException("La mascota seleccionada no existe.");
		}
		
		Optional<Mascota> respuesta2 = mascotaRepository.findById(idMascota1);
		
		if (respuesta2.isPresent()) {
			Mascota mascota2 = respuesta2.get();
			voto.setMascota2(mascota2);
		} else {
			throw new ErrorServiceException("La mascota seleccionada no existe.");
		}
		
		votoRepository.save(voto);
		
	}
	
	public void responder(String idUsuario, String idVoto) throws ErrorServiceException  {
		
		Optional<Voto> respuesta = votoRepository.findById(idVoto);
		
		if(respuesta.isPresent()) {
			Voto voto = respuesta.get();
			if (voto.getMascota2().getUsuario().getId().equals(idUsuario)) {
				voto.setRespuesta(new Date()); 
			} else {
				throw new ErrorServiceException("No tiene permisos para realizar esta operación");
			}
		} else {
			throw new ErrorServiceException("No se encontró el voto seleccionado.");
		}
		
	}
		
}
