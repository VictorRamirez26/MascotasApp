package com.is2.MascotasApp.entities;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Zona {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String nombre;
	private boolean eliminado;
}
