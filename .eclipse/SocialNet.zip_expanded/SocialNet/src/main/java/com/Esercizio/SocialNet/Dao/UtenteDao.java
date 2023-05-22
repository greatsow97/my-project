package com.Esercizio.SocialNet.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Esercizio.SocialNet.Model.Utente;

public interface UtenteDao extends JpaRepository<Utente, Integer> {

	@Query(value="select * from utenti where username = :username", nativeQuery = true)
	Utente userLogin(String username);
	
	@Query(value="select * from utenti where id_ruolo = 3 or id_ruolo = 2 ",nativeQuery = true)
	List<Utente>utentiReg();
	
	@Modifying
	@Query(value="update utenti set attivo = 1, fine_ban = null where fine_ban is not null and fine_ban < :now", nativeQuery = true)
	void reactiveUsers(Long now);
	
	@Query(value="select * from utenti where uid = :uid", nativeQuery = true)
	Utente uid(String uid);
	
	@Query(value = "select * from utenti where email= :email", nativeQuery = true)
	Utente email(String email);

	
	
}
