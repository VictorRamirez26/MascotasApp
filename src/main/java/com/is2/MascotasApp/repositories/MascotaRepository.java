package com.is2.MascotasApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.is2.MascotasApp.entities.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, String>{

	@Query("SELECT m FROM Mascota m WHERE m.usuario.id = :id ")
	public List<Mascota> buscarMascotaPorUsuario(@Param("id") String id);
}
