package com.prog.presentazione.Model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="storico")
public class Storico {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	Date dataprestito;
	
	@ManyToOne
	@JoinColumn(name = "id_utente",referencedColumnName = "id")
	Utente utente;
	
	@ManyToOne
	@JoinColumn(name = "id_libro",referencedColumnName = "id")
	Libro libro;

	public Storico() {
		// TODO Auto-generated constructor stub
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Date getDataprestito() {
		return dataprestito;
	}

	public void setDataprestito(Date dataprestito) {
		this.dataprestito = dataprestito;
	}
	
	
	
}
