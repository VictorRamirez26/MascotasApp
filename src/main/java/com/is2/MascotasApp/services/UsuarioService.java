package com.is2.MascotasApp.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Foto;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.entities.Zona;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private FotoService fotoService;
	
	@Autowired
	private ZonaService zonaService;
	
	@Autowired
	private NotificacionService notificacionService;
	
	@Transactional
	public void crear(String nombre, String apellido, String email, String clave1, String clave2,
			MultipartFile archivo, String idZona) 
			throws ErrorServiceException, IOException, Exception{

		Zona zona = zonaService.buscarZonaPorId(idZona);
		validar(nombre,apellido,email,clave1, clave2,zona);
		
		String claveEncriptada = new BCryptPasswordEncoder().encode(clave1);
		
		Usuario usuario = Usuario.builder().nombre(nombre).apellido(apellido)
				.email(email).clave(claveEncriptada).alta(new Date()).zona(zona).build();
		
		Foto foto = fotoService.guardar(archivo);
		usuario.setFoto(foto);
		
		usuarioRepository.save(usuario);
		
		//notificacionService.enviar("Bienvenido al Tinder de mascota", "Tinder de mascota", usuario.getEmail());
		
	}
	
	@Transactional
	public void modificar(String id, String nombre, String apellido, String email, String clave1, String clave2,
			MultipartFile archivo, String idZona) 
			throws ErrorServiceException, IOException, Exception{
		
		Optional<Usuario> respuesta = usuarioRepository.findById(id);
		
		if (respuesta.isPresent()) {
			
			Zona zona = zonaService.buscarZonaPorId(idZona);
			
			validar(nombre, apellido, email, clave1 , clave2,zona);
			
			Usuario usuario = respuesta.get();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			usuario.setZona(zona);
			
			String claveEncriptada = new BCryptPasswordEncoder().encode(clave1);
			usuario.setClave(claveEncriptada);
			
			Foto foto = fotoService.actualizar(usuario.getFoto().getId(), archivo);
			usuario.setFoto(foto);
			
			usuarioRepository.save(usuario);
			
		}else {
			throw new ErrorServiceException("Usuario no encontrado");
		}
		
	}
	
	@Transactional
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
	
	private void validar(String nombre, String apellido, String email, String clave1, String clave2, Zona zona) throws ErrorServiceException{
		
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServiceException("Ingrese el nombre.");
		}
		
		if (apellido == null || apellido.isEmpty()) {
			throw new ErrorServiceException("Ingrese el apellido.");
		}
		
		if (email == null || email.isEmpty()) {
			throw new ErrorServiceException("Ingrese el email.");
		}
		
		if (zona == null) {
			throw new ErrorServiceException("Ingrese la zona.");
		}
		
		if (clave1 == null || clave1.isEmpty()) {
			throw new ErrorServiceException("Ingrese su clave.");
		}
		
		if (clave1.length() <= 6) {
			throw new ErrorServiceException("Su clave debe tener más de 6 dígitos.");
		}
		
		if (clave2 == null || clave2.isEmpty()) {
			throw new ErrorServiceException("Debe repetir su contraseña.");
		}
		
		if (!clave1.equals(clave2)) {
			throw new ErrorServiceException("Las contraseñas ingresadas deben ser iguales.");
		}
		
	}

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.buscarUsuarioPorMail(mail);
		if(usuario != null) {
			List<GrantedAuthority> permisos = new ArrayList<>();
			
			GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
			GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_MASCOTAS");
			GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_VOTOS");
			
			permisos.add(p1);
			permisos.add(p2);
			permisos.add(p3);
			
			User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
			return user;
		}
		return null;
	}
	
	
}
