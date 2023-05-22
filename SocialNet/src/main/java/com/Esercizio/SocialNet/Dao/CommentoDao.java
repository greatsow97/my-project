package com.Esercizio.SocialNet.Dao;



import org.springframework.data.jpa.repository.JpaRepository;


import com.Esercizio.SocialNet.Model.Commento;


public interface CommentoDao extends JpaRepository<Commento, Integer> {

	
}
