package com.is2.MascotasApp.entities;

import java.util.Date;

import com.is2.MascotasApp.enums.Sexo;
import com.is2.MascotasApp.enums.Tipo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mascota {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String nombre;
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private Tipo tipo;
	
	@Temporal(TemporalType.TIME)
	private Date alta;
	@Temporal(TemporalType.TIME)
	private Date baja;
	
	@ManyToOne
	private Usuario usuario;
	
	@OneToOne
	private Foto foto;
	
}
