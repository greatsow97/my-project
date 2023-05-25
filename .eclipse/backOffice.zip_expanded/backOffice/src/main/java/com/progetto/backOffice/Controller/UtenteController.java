package com.progetto.backOffice.Controller;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.progetto.backOffice.Dao.UtenteDao;
import com.progetto.backOffice.model.DatiJson;
import com.progetto.backOffice.model.Utente;
import com.progetto.backOffice.utilis.MailUtilis;



@Controller
public class UtenteController {

	@Autowired
	UtenteDao uDao;
	@Autowired
	BCryptPasswordEncoder encoder;

	/**
	 * -------------------> primo accesso <------------------
	 * 
	 * */
	
	@GetMapping("/")
	public String main2(Model m) {
		m.addAttribute("msg", "");

		return "start";
	}
	
	@GetMapping("/primoAc")
	public String primoAccesso(Model m) {
		m.addAttribute("msg", "");

		return "accediRec";
	}
	
	@PostMapping("/accediRec")
	public String main(@RequestParam String username, @RequestParam String password, Model m) {
		Utente u = uDao.login(username);

		if (u == null) {
			m.addAttribute("msg", "utente non trovato");
			return "reclutatore";
		}

		if (!encoder.matches(password, u.getPassword())) {
			u.setUid(UUID.randomUUID().toString().substring(0, 25));
			uDao.save(u);
			return "redirect:/creaPassword/" + u.getUid();
		}
		
		
		
		
		return "redirect:/paginaIniziale";
	}
	
	

	/**
	 * ---------------------->LOGIN <-----------------------
	 */
	
	@GetMapping("/loggato")
	public String loggato(Model m) {
		m.addAttribute("msg", "");

		return "reclutatore";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model m) {
		Utente u = uDao.login(username);
		
		System.out.println(u);
		if (u == null) {
			m.addAttribute("msg", "utente non trovato");
			return "reclutatore";
		}

		if (!encoder.matches(password, u.getPassword())) {
			u.setUid(UUID.randomUUID().toString().substring(0, 25));
			m.addAttribute("msg", "password non coretto");
		return "reclutatore";	}
		
		
		return "redirect:/paginaIniziale";
	}
	

	

	/**
	 * -------------------------> RECUPERO PASSWORD * <--------------------------------
	 */

	@GetMapping("/recuperoPas")
	public String recuperoPassword(Model m) {
		m.addAttribute("msg1", "");
		
	
		return "recuperoPas";
	}

	@PostMapping("/sendEm")
	public String sendEmail(@RequestParam String email, @RequestParam String username, Model m) {
		Utente u = uDao.sendEmail(username, email);
		
		if (u == null) {
			m.addAttribute("msg1", "email non esiste");
			return "recuperoPas";
		}else {
			u.setUid(UUID.randomUUID().toString().substring(0, 25));
			uDao.save(u);
			MailUtilis.sendEmail(email, "link per recupero password", u.getUid());
			m.addAttribute("msg1", "controlla l'email");
			return "recuperoPas";
		}

		
	}

	@GetMapping("/creaPassword/{utenteId}")
	public String creaPassword(@PathVariable String utenteId) {
		Utente u = uDao.uid(utenteId);
		uDao.save(u);
		
		return "creaPassword";
	}

	@GetMapping("/newPwd/{utenteId}")
	public String newPwd(@PathVariable String utenteId,@RequestParam String password) {
		Utente u = uDao.uid(utenteId);
		u.setUid(null);
		u.setPassword(password);
		String rawPassword = u.getPassword();
		String encodePassword = encoder.encode(rawPassword);
		u.setPassword(encodePassword);
		
		uDao.save(u);
		
		return "redirect:/loggato";
	}

	/**
	 * ---------------------->PAGINA INIZIALE <-----------------------
	 */
	@GetMapping("/paginaIniziale")
	public String paginaIniziale() {

		return "paginaIniziale";
	}

	/**
	 * ---------------------->CREA OFFERTE <--------------
	 */
	@PostMapping("/creaOfferta")
	public String offerta(@RequestParam String[] what, @RequestParam String[] requisiti,
			@RequestParam String[] cosa_offriamo, @RequestParam String modalita, @RequestParam String title,
			@RequestParam String subTitle, @RequestParam String subSubTitle, @RequestParam String[] soft_skills,
			@RequestParam String[] nice_to_have) {

		try {
			File recjasonFile = new File("src/main/resources/data.json");
			ObjectMapper obj = new ObjectMapper();
			CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class,
					DatiJson.class);
			List<DatiJson> existingData = obj.readValue(recjasonFile, listType);

			DatiJson djs = new DatiJson();
			djs.setWhat(what);
			djs.setRequisiti(requisiti);
			djs.setCosa_offriamo(cosa_offriamo);
			djs.setModalita(modalita);
			djs.setTitle(title);
			djs.setSubTitle(subTitle);
			djs.setSubSubTitle(subSubTitle);
			djs.setSoft_skills(soft_skills);
			djs.setNice_to_have(nice_to_have);

			existingData.add(djs);
			obj.enable(SerializationFeature.INDENT_OUTPUT);
			obj.writeValue(recjasonFile, existingData);

		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/paginaIniziale";
	}

	/**
	 * -------------------> MODIFICA <---------------------
	 * 
	 * @throws IOException
	 * @throws DatabindException
	 * @throws StreamReadException
	 */
	@GetMapping("/modifica/{singleOfferta}")
	public String modifica(@PathVariable Integer singleOfferta, Model m)
			throws StreamReadException, DatabindException, IOException {

		ObjectMapper obj = new ObjectMapper();
		File recjasonFile = new File("src/main/resources/data.json");
		List<DatiJson> rec = obj.readValue(recjasonFile, new TypeReference<List<DatiJson>>() {
		});

		m.addAttribute("offerta", rec.get(singleOfferta));

		m.addAttribute("offerta");
		return "modifica";
	}

	/**
	 * 
	 * ---------------------------- SALVARE -----------------------
	 */

	@PostMapping("/salvaOfferta/{singleOfferta}")
	public String aggiornaOfferta(@PathVariable Integer singleOfferta, @RequestParam String[] what,
			@RequestParam String[] requisiti, @RequestParam String[] cosa_offriamo, @RequestParam String modalita,
			@RequestParam String title, @RequestParam String subTitle, @RequestParam String subSubTitle,
			@RequestParam String[] soft_skills, @RequestParam String[] nice_to_have)
			throws StreamReadException, DatabindException, IOException {
		ObjectMapper obj = new ObjectMapper();
		File recjasonFile = new File("src/main/resources/data.json");
		List<DatiJson> rec = obj.readValue(recjasonFile, new TypeReference<List<DatiJson>>() {
		});

		DatiJson djs = rec.get(singleOfferta);
		djs.setWhat(what);
		djs.setRequisiti(requisiti);
		djs.setCosa_offriamo(cosa_offriamo);
		djs.setModalita(modalita);
		djs.setTitle(title);
		djs.setSubTitle(subTitle);
		djs.setSubSubTitle(subSubTitle);
		djs.setSoft_skills(soft_skills);
		djs.setNice_to_have(nice_to_have);

		rec.set(singleOfferta, djs);
		obj.writeValue(recjasonFile, rec);
		return "redirect:/offerta/" + singleOfferta;
	}

	/**
	 * ----------------------LISTA OFFERTE----------------------
	 */
	@GetMapping("/offerte")
	public String listaOfferte(Model m) throws StreamReadException, DatabindException, IOException {

		/** --------lista di offerte via jason------------ */
		ObjectMapper obj = new ObjectMapper();
		File recjasonFile = new File("src/main/resources/data.json");
		List<DatiJson> dj = obj.readValue(recjasonFile, new TypeReference<List<DatiJson>>() {
		});
		m.addAttribute("offerte", dj);

		return "listaOfferte";
	}

	/**
	 * 
	 * ---------------------------------------------CANCELLA
	 * OFFERTA-------------------
	 */
	@GetMapping("/CancellaOfferta/{singleOfferta}")
	public String CancellaOfferta(@PathVariable Integer singleOfferta)
			throws StreamReadException, DatabindException, IOException {

		ObjectMapper obj = new ObjectMapper();
		File recjasonFile = new File("src/main/resources/data.json");
		List<DatiJson> rec = obj.readValue(recjasonFile, new TypeReference<List<DatiJson>>() {
		});

		rec.remove((int) singleOfferta);

		obj.writeValue(recjasonFile, rec);

		return "redirect:/offerte";
	}

	/**
	 * ----------------------------------------------SIGNOLO
	 * OFFERTA----------------------------
	 */

	@GetMapping("/offerta/{singleOfferta}")
	public String offerta(@PathVariable Integer singleOfferta, Model m) {

		try {
			ObjectMapper obj = new ObjectMapper();
			File recjasonFile = new File("src/main/resources/data.json");
			List<DatiJson> rec = obj.readValue(recjasonFile, new TypeReference<List<DatiJson>>() {
			});

			m.addAttribute("offerta", rec.get(singleOfferta));

		} catch (StreamReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "offerta";
	}
}
