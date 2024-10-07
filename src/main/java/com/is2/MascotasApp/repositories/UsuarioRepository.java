package com.is2.MascotasApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.is2.MascotasApp.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
	
	@Query("SELECT u FROM Usuario u WHERE u.email = :email")
	public Usuario buscarUsuarioPorMail(@Param("email") String email);

}
