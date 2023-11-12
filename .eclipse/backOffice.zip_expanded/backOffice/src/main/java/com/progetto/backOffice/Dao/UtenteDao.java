package com.progetto.backOffice.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.progetto.backOffice.model.Utente;

public interface UtenteDao extends JpaRepository<Utente, Integer> {

	@Query(value="select * from utenti where username = :username",nativeQuery = true)
	Utente login(String username);

	
	@Query(value="select * from utenti where email = :email and username = :username", nativeQuery = true)
	Utente sendEmail (String username,String email);
	
	
	@Query(value="select * from utenti where uid = :uid", nativeQuery = true)
	Utente uid(String uid);
}
