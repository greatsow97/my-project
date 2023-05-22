package com.studenti.studenti.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studenti.studenti.models.Studente;

public interface StudenteDao extends JpaRepository<Studente, Integer>{

}
