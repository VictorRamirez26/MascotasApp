package com.is2.MascotasApp.entities;

import java.util.Date;

import com.is2.MascotasApp.enums.Sexo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Mascota {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String nombre;
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	@Temporal(TemporalType.TIME)
	private Date alta;
	@Temporal(TemporalType.TIME)
	private Date baja;
	
	@ManyToOne
	private Usuario usuario;
	
}
