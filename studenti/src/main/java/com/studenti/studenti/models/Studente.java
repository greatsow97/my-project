/**
 * 
 * @author Sow
 * 
 * @description project form Studenti
 * 
 * */
package com.studenti.studenti.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="studenti")
// serve a collegare la classe java al DB
public class Studente {

	@Id
	// mi crea un chiave valore primaria per id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	String nome, cognome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	
	
	
}
