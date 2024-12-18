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
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.is2.MascotasApp.entities.Foto;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.entities.Zona;
import com.is2.MascotasApp.error.ErrorServiceException;
import com.is2.MascotasApp.repositories.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
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
	
	public Usuario buscarUsuarioPorEMail(String email) {
	    return usuarioRepository.buscarUsuarioPorMail(email);
	}


	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioRepository.buscarUsuarioPorMail(mail);
		
		if(usuario != null) {
			List<GrantedAuthority> permisos = new ArrayList<>();
			
			GrantedAuthority p1 = new SimpleGrantedAuthority("ROL_USUARIO_REGISTRADO");
			
			permisos.add(p1);

			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attr.getRequest().getSession(true);
			session.setAttribute("usuarioSession", usuario);
			
			User user = new User(usuario.getEmail(), usuario.getClave(), permisos);
			return user;
		}
		return null;
	}
	
	public Usuario crearDesdeOAuth(DefaultOAuth2User oauth2User) {
		
		System.out.println("Entre a crearDesdeOAuth");
	    String nombre = oauth2User.getAttribute("name");
	    String email = oauth2User.getAttribute("email");

	    // Aquí se asigna una contraseña genérica, ya que OAuth2 se encarga de la autenticación.
	    String claveEncriptada = new BCryptPasswordEncoder().encode("passwordGenerica");

	    Usuario usuario = Usuario.builder()
	            .nombre(nombre)
	            .email(email)
	            .clave(claveEncriptada)  // Contraseña genérica
	            .alta(new Date())
	            .build();

	    usuarioRepository.save(usuario);  // Guarda el usuario en la base de datos
	    return usuario;
	}
	
	public Usuario buscarUsuarioPorId(String id){
		
		Optional<Usuario> respuesta = usuarioRepository.findById(id);
		if (respuesta.isPresent()) {
			return respuesta.get();
		}
		return null;
	}
	
	
}
