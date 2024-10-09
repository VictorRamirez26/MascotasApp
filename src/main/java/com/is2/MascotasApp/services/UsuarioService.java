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
	private NotificacionService notificacionService;
	
	public void crear(String nombre, String apellido, String email, String clave, MultipartFile archivo) 
			throws ErrorServiceException, IOException, Exception{
		
		validar(nombre,apellido,email,clave);
		
		String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
		
		Usuario usuario = Usuario.builder().nombre(nombre).apellido(apellido)
				.email(email).clave(claveEncriptada).alta(new Date()).build();
		
		Foto foto = fotoService.guardar(archivo);
		
		usuario.setFoto(foto);
		usuarioRepository.save(usuario);
		
		notificacionService.enviar("Bienvenido al Tinder de mascota", "Tinder de mascota", usuario.getEmail());
		
	}
	
	@Transactional
	public void modificar(String id, String nombre, String apellido, String email, String clave, MultipartFile archivo) 
			throws ErrorServiceException, IOException, Exception{
		
		Optional<Usuario> respuesta = usuarioRepository.findById(id);
		
		if (respuesta.isPresent()) {
			
			validar(nombre, apellido, email, clave);
			Usuario usuario = respuesta.get();
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setEmail(email);
			String claveEncriptada = new BCryptPasswordEncoder().encode(clave);
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
