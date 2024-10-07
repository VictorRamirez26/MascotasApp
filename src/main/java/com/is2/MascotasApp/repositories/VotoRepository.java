package com.is2.MascotasApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.is2.MascotasApp.entities.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, String>{

	@Query("SELECT v FROM Voto v WHERE v.mascota1.id = :idMascota "
			+ "ORDER BY v.fecha DESC")
	public List<Voto> listarVotosGenerados(@Param("idMascota") String idMascota);
	
	@Query("SELECT v FROM Voto v WHERE v.mascota2.id = :idMascota "
			+ "ORDER BY v.fecha DESC")
	public List<Voto> listarVotosRecibidos(@Param("idMascota") String idMascota);
}
