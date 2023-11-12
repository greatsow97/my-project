package com.prog.presentazione.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prog.presentazione.Model.Utente;

public interface UtenteDao extends JpaRepository<Utente, Integer>{

	@Query(value="SELECT * FROM utenti WHERE utenti.username = :username",nativeQuery = true)
	Utente logged(String username);
	
	@Query(value="select * from utenti where email = :email",nativeQuery = true)
	Utente sendEm(String email);
		
}
