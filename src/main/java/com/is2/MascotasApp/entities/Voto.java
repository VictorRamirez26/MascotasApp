package com.is2.MascotasApp.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Voto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Temporal(TemporalType.TIME)
	private Date fecha;
	@Temporal(TemporalType.TIME)
	private Date respuesta;
	
	@ManyToOne
	private Mascota mascota1; // Mascota que origina el voto
	@ManyToOne
	private Mascota mascota2; // Mascota que recibe el voto
	
}
