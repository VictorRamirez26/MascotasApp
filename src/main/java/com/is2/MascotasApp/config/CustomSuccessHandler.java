package com.is2.MascotasApp.config;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.is2.MascotasApp.entities.Usuario;
import com.is2.MascotasApp.services.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("Entre a onAuthenticationSuccess");

        // Verifica si el usuario ha iniciado sesión con OAuth2
        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");

            // Verifica si el usuario ya está registrado en la base de datos
            Usuario usuario = usuarioService.buscarUsuarioPorEMail(email);
            if (usuario == null) {
                // Si el usuario no está registrado, lo crea automáticamente
                usuario = usuarioService.crearDesdeOAuth(oauth2User); // Método que debes agregar en tu servicio
            }

            // Establece el usuario en la sesión
            request.getSession().setAttribute("usuarioSession", usuario);
        }

        System.out.println("Sali de onAuthenticationSuccess");

        // Redirige al usuario a la página principal después de iniciar sesión
        new DefaultRedirectStrategy().sendRedirect(request, response, "/usuario");
    }
}
