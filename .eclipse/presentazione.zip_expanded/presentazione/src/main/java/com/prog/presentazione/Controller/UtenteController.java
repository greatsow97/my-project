package com.prog.presentazione.Controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;


import com.prog.presentazione.Dao.LibroDao;
import com.prog.presentazione.Dao.RuoloDao;
import com.prog.presentazione.Dao.UtenteDao;


import com.prog.presentazione.Model.Libro;
import com.prog.presentazione.Model.Utente;
import com.prog.presentazione.utilis.MailUtilis;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UtenteController {

	@Autowired
	UtenteDao uDao;
	@Autowired
	LibroDao lDao;
	@Autowired 
	RuoloDao rDao;


	@PostMapping("/login")
	public Utente login(@RequestBody Utente u) {
		Utente u2 = uDao.logged(u.getUsername());

		return u2;
	}

	@PostMapping("/send")
	public Utente sendMail(@RequestBody Utente u2) {
		Utente u = uDao.sendEm(u2.getEmail());

		if (u != null) {
			MailUtilis.sendMail(u2.getEmail(), "cambia password", u.getId());
			return u;
		}
		return u;

	}

	@GetMapping("/bookList")
	public List<Libro> bookList() {

		return lDao.findAll();
	}

	
//	PROVA INSERIMENTO IMMAGINE
	
	@PostMapping("/upload/{libroID}")
	public Integer saveImage(@PathVariable Integer libroID, @RequestBody Libro l ) {
		
		try {
			 Libro l2 = lDao.findById(libroID).get();
			 l2.setImg(l.getImg());
			lDao.save(l2);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}		
	}
	
	@PostMapping("/newUser")
	public Integer createUser(@RequestBody Utente u2) {
		
		try {
			u2.setRuolo(rDao.findById(3).get());
			uDao.save(u2);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
				
	}
	
	@GetMapping("/listaUser")
	public List<Utente> listaUtenti() {
		
		return uDao.findAll();
	}
	
	
	@PostMapping("/takeBook/{libroId}")
	public Libro prendiLibro(@PathVariable Integer libroId,@RequestBody Utente u) {
		
		Libro l = lDao.findById(libroId).get();
		
		
		LocalDate dateL =LocalDate.now().plusMonths(1);
		Date dt = Date.valueOf(dateL);
		l.setFineScad(dt);
		
		l.setUtente(uDao.findById(u.getId()).get());
		lDao.save(l);
		return l;
	}
	
	@PostMapping("/stopTake")
	public Integer stopTake(@RequestBody Utente u) {
//	ricorda di non passare l'intero oggeto su l'id altrimenti non prende
		
		return lDao.libroCount(u.getId());
	}
	
	@PostMapping("/newBook")
	public Integer createBook(@RequestBody Libro l) {
		
		try {
			
			lDao.save(l);
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
		
	}
	
	@PostMapping("/newPwd/{utenteId}")
	public Integer newPwd( @PathVariable Integer utenteId, @RequestBody Utente u) {
		
		try {
//			senza l'istanziamneto del nuovo utente mi salvava come un utente vuoto
			//senza questo istanziamento intendevo
			 Utente u2 = uDao.findById(utenteId).get();
		u2.setPassword(u.getPassword());
			 
			uDao.save(u2);
			
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
	}
	
	@GetMapping("/singleUser/{utenteId}")
	public Utente SingleUser(@PathVariable Integer utenteId) {
		
		return uDao.findById(utenteId).get();
	}
	
	@PostMapping("/promote/{utenteId}")
	public Integer promoteUser(@PathVariable Integer utenteId) {
		
		try {
			Utente u = uDao.findById(utenteId).get();
			
			if (u.getRuolo().getId() == 2) {
				u.setRuolo(rDao.findById(3).get());
			}else {
				u.setRuolo(rDao.findById(2).get());
			}
			uDao.save(u);
			
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
		
		return 1;
		}
		
	}
	
	@PostMapping("/revokeSend")
	public Integer revoke(String recipient,String body) {
		try {
			
			MailUtilis.sendMail2(recipient, body);
			
			return 0;
		} catch (Exception e) {
			// TODO: handle exception
			return 1;
		}
		
	}
	
	
	
}
