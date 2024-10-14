package com.is2.MascotasApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.is2.MascotasApp.entities.Zona;
import com.is2.MascotasApp.repositories.ZonaRepository;

@Service
public class ZonaService {

	@Autowired
	private ZonaRepository zonaRepository;
	
	public void crear(String nombre) {
		Zona zona = Zona.builder().nombre(nombre).eliminado(false).build();
		zonaRepository.save(zona);
	}
	
	public void modificar(String idZona, String nombre) {
		Optional<Zona> respuesta = zonaRepository.findById(idZona);
		if (respuesta.isPresent()) {
			Zona zona = respuesta.get();
			zona.setNombre(nombre);
			zonaRepository.save(zona);
		}
	}
	
	public void eliminar(String idZona) {
		
		Optional<Zona> respuesta = zonaRepository.findById(idZona);
		if (respuesta.isPresent()) {
			Zona zona = respuesta.get();
			zona.setEliminado(true);;
			zonaRepository.save(zona);
		}
	}
	
	public Zona buscarZonaPorId(String idZona) {
		Optional<Zona> respuesta = zonaRepository.findById(idZona);
		if(respuesta.isPresent()) {
			return respuesta.get();
		}
		return null;
	}
	
	public List<Zona> listarZonasActivas() {
		return zonaRepository.listarZonasActivas();
	}
	
	
}
