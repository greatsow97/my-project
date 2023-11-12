package com.prog.presentazione.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.prog.presentazione.Model.Libro;

import jakarta.transaction.Transactional;
 
@Transactional
public interface LibroDao extends JpaRepository<Libro, Integer> {

	
	@Query(value="select count(id_utente) from libri where id_utente = :utenteID",nativeQuery = true)
	Integer libroCount(Integer utenteID);
	
	@Modifying
	@Query(value="UPDATE libri INNER JOIN utenti ON libri.id_utente = utenti.id  SET utenti.access = 1 WHERE fine_scad < NOW()",nativeQuery = true)
	void AccessDenied();
	
}
