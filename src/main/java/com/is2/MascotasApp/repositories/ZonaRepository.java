package com.is2.MascotasApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.is2.MascotasApp.entities.Zona;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, String>{

}
