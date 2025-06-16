package com.hibernate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name="plazas")
public class Planta {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column (name="id")
	int id;
	@Column (name="planta")
	int numplanta;
	@Column (name="plazasLibres")
	int plazas;
	
	public Planta() {
		super();
	}

	public Planta(int numplanta, int plazas) {
		super();
		this.numplanta = numplanta;
		this.plazas = plazas;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumplanta() {
		return numplanta;
	}

	public void setNumplanta(int numplanta) {
		this.numplanta = numplanta;
	}

	public int getPlazas() {
		return plazas;
	}

	public void setPlazas(int plazas) {
		this.plazas = plazas;
	}
	
}
