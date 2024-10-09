package com.is2.MascotasApp.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Foto;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.FotoRepository;

import jakarta.transaction.Transactional;

@Service
public class FotoService {

	@Autowired
	private FotoRepository fotoRepository;

	@Transactional
	public Foto guardar(MultipartFile archivo) throws ErrorServiceException, IOException, Exception {
		if (archivo != null) {
			try {
				Foto foto = Foto.builder().mime(archivo.getContentType()).nombre(archivo.getName())
						.contenido(archivo.getBytes()).build();

				return fotoRepository.save(foto);

			} catch (Exception e) {
				throw new Exception("Error de sistemas");
			}

		} else {
			throw new ErrorServiceException("Debe agregar una foto");
		}
	}
	
	@Transactional
	public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServiceException, Exception{
		if (archivo != null) {
			try {
				Foto foto = Foto.builder().build();
				if(idFoto != null) {
					Optional<Foto> respuesta = fotoRepository.findById(idFoto);
					if(respuesta.isPresent()) {
						foto = respuesta.get();
					}
				}
				foto.setMime(archivo.getContentType());
				foto.setNombre(archivo.getName());
				foto.setContenido(archivo.getBytes());
				
				return fotoRepository.save(foto);
			} catch (Exception e) {
				throw new Exception("Error de sistemas");
			}

		} else {
			throw new ErrorServiceException("Debe agregar una foto");
		}
	}
}
