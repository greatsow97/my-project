package com.studenti.studenti.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.studenti.studenti.Dao.StudenteDao;
import com.studenti.studenti.models.Studente;

/**
 * @author Sow
 * */


@Controller
public class StudentiControllers {

	/**
	 * serve a poter controllare il Dao creazione delle liste e altre azioni deve
	 * esserci!
	 */
	@Autowired
	StudenteDao sDao;

	@GetMapping("/")
	public String main() {

		return "main";
	}
	
	@GetMapping("/list")
	public String list(Model m) {
		
		/**
		 * e' la query selcet*from Studenti
		 * */
		List<Studente> list = sDao.findAll();
		
		/*
		 * -la prima serve ad mettere nel html objetct
		 * ma come nome tra virgolette posso mettere quello che voglio
		 * 
		 *- la seconda fa riferimento alla lista creata sopra
		 * m.addAttribute("",list);
		 * */
		m.addAttribute("list",list);
		
		return "list";
	}
	
	@GetMapping("/insert")
	public String create(Model m) {
		m.addAttribute("studente",new Studente());
		
		return "insert";
		
	}
	
	@PostMapping("/nuovoStudente")
	public String createStudent(Studente studente) {
		sDao.save(studente);
		return "redirect:/list";
	}

}
