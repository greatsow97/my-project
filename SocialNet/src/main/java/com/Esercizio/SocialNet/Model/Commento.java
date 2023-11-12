package com.Esercizio.SocialNet.Model;

import java.sql.Date;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="commenti")
public class Commento {


	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer id;
	
	String commento;
	



	@ManyToOne
	@JoinColumn(name="id_utente",referencedColumnName = "id")
	Utente utente;
	

	@ManyToOne
	@JoinColumn(name="id_post",referencedColumnName = "id")
	Post post;
	
	Integer visto;
	
	@Column(name="data_commento")
	Date dataCommento;
	
	
	public Commento() {
		// TODO Auto-generated constructor stub
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCommento() {
		return commento;
	}


	public void setCommento(String commento) {
		this.commento = commento;
	}

	public Utente getUtente() {
		return utente;
	}


	public void setUtente(Utente utente) {
		this.utente = utente;
	}


	public Post getPost() {
		return post;
	}


	public void setPost(Post post) {
		this.post = post;
	}


	public Integer getVisto() {
		return visto;
	}


	public void setVisto(Integer visto) {
		this.visto = visto;
	}


	public Date getDataCommento() {
		return dataCommento;
	}


	public void setDataCommento(Date dataCommento) {
		this.dataCommento = dataCommento;
	}
	
	
	
}
