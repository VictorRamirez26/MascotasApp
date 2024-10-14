package com.is2.MascotasApp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.is2.MascotasApp.entities.Zona;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, String>{

	@Query("SELECT z FROM Zona z WHERE z.eliminado = FALSE")
	public List<Zona> listarZonasActivas();
}
