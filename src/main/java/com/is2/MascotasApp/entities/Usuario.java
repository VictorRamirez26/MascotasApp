package com.is2.MascotasApp.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String nombre;
	private String apellido;
	private String email;
	private String clave;
	@ManyToOne
	private Zona zona;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date alta;
	@Temporal(TemporalType.TIMESTAMP)
	private Date baja;
	
	@OneToOne
	private Foto foto;
}
