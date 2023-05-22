package com.Esercizio.SocialNet.Controller;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.util.StringUtils;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Esercizio.SocialNet.Dao.CommentoDao;
import com.Esercizio.SocialNet.Dao.PostDao;
import com.Esercizio.SocialNet.Dao.RuoloDao;
import com.Esercizio.SocialNet.Dao.UtenteDao;
import com.Esercizio.SocialNet.Model.Commento;
import com.Esercizio.SocialNet.Model.Post;
import com.Esercizio.SocialNet.Model.Utente;
import com.Esercizio.SocialNet.Utilis.FileUploadUtil;
import com.Esercizio.SocialNet.Utilis.MailUtilis;

import jakarta.servlet.http.HttpSession;

@Controller
public class UtenteController {

	@Autowired
	UtenteDao uDao;
	@Autowired
	RuoloDao rDao;
	@Autowired
	PostDao pDao;
	@Autowired
	CommentoDao cDao;

	@Autowired
	BCryptPasswordEncoder encoder;

	@GetMapping("/")
	public String main(Model m) {

		m.addAttribute("msg", "");
		return "main";
	}

	/**
	 * --------------------LOGIN-------------
	 * 
	 */

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model m) {

		Utente u = uDao.userLogin(username);

		if (u == null) {
			m.addAttribute("msg", "utente non trovato");
			return "main";
		}

		if (u.getAttivo() == 0) {
			m.addAttribute("msg", "sei stato bannato o il tuo email è inesistente");
			return "main";
		}

		if (!encoder.matches(password, u.getPassword())) {
			m.addAttribute("masg", "password errata");
			return "main";
		}

		Hibernate.initialize(u.getPost());
		for (Post p : u.getPost()) {
			Hibernate.initialize(p.getCommenti());
		}

		session.setAttribute("utenteLoggato", u);

		return "redirect:/areaPersonale";
	}

	@GetMapping("/areaPersonale")
	public String areaPersonale(Model m, HttpSession session) {
		Utente u = (Utente) session.getAttribute("utenteLoggato");

		if (u == null) {
			return "redirect:/";
		}

		m.addAttribute("notifiche", pDao.notifications(u.getId()));
		
		m.addAttribute("postUtente",u);

		return "areaPersonale";
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();

		return "redirect:/";
	}

	/**
	 * -------------------REGISTRAZIONE-----------------
	 * 
	 */

	@GetMapping("/registrazione")
	public String registra(Model m) {

		m.addAttribute("utente", new Utente());
		return "registratzione";
	}
	
	
	@PostMapping("/creaUtente")
	public String creaUtente(@RequestParam String nome, @RequestParam String cognome, @RequestParam String username,
			@RequestParam String email, @RequestParam String password) {

		Utente u = new Utente();
		u.setNome(nome);
		u.setCognome(cognome);
		u.setUsername(username);
		u.setEmail(email);
		u.setPassword(password);
		String rawPassword = u.getPassword();
		String encodePassword = encoder.encode(rawPassword);
		u.setPassword(encodePassword);

		u.setUid(UUID.randomUUID().toString());

		u.setRuolo(rDao.findById(3).get());
		uDao.save(u);

		MailUtilis.sendMail(email, "ciao la tua registrazione è stata confermata", u.getUid());

		return "redirect:/";
	}

	// serve un id utente
	@GetMapping("/prova/{utenteId}")
	public String PROVA(@PathVariable String utenteId) {
		Utente u = uDao.uid(utenteId);
		u.setAttivo(1);
		uDao.save(u);
		return "prova";
	}

	
	/**
	 * -----------------------PASSWORD DIMENTICATA-----------------------
	 * */
	
	@GetMapping("/dimenticato")
	public String mandaCodice() {
		
		return "dimenticato";
	}
	
	@PostMapping("/inviaEmail")
	public String sendLink(Model m, @RequestParam String email) {
		Utente u = uDao.email(email);
		
		
		MailUtilis.sendMailforP(email,"crea Password clicca link",u.getUid());
		
		m.addAttribute("emailSent",true);
		return "redirect:/dimenticato";
	}
	
	@GetMapping("/recuperoPassword/{utenteID}")
	public String recuperoPassword(@PathVariable String utenteID,Model m) {
		 
		m.addAttribute("utente",uDao.uid(utenteID));
		return "recuperoPassword";
	}
	
	@PostMapping("/creaPassword/{utenteID}")
	public String creaPassword(@RequestParam String password,@PathVariable String utenteID) {
		Utente u = uDao.uid(utenteID);
		u.setPassword(password);
		String rawPassword = u.getPassword();
		String encodePassword = encoder.encode(rawPassword);
		u.setPassword(encodePassword);
		
		uDao.save(u);
		
		
		return "redirect:/";
	}
	
	
	/**
	 * -----------------LISTA UTENTI--------
	 * 
	 */

	@GetMapping("/listaUtenti")
	public String listaUtenti(Model m) {

		m.addAttribute("list", uDao.utentiReg());

		return "listaUtenti";
	}

	/**
	 * ---------------POST-----------------
	 */

	@GetMapping("/listaPost")
	public String listaPost(Model m) {
		m.addAttribute("listaPost", pDao.findAll());
		return "listaPost";
	}

	@PostMapping("/creaPost")
	public String creaPost(HttpSession session, @RequestParam String testo, @RequestParam String titolo) {
		Post post = new Post();
		Utente u = (Utente) session.getAttribute("utenteLoggato");
		post.setUtente(u);
		post.setTitolo(titolo);
		post.setTesto(testo);
		post.setDataPost(Date.valueOf(LocalDate.now()));
		pDao.save(post);

		return "redirect:/listaPost";
	}

	@GetMapping("/ilPost/{postId}")
	public String notificaPost(@PathVariable Integer postId, Model m, HttpSession session) {
		Utente u = (Utente) session.getAttribute("utenteLoggato");

		Post p = pDao.findById(postId).get();
		if (p.getUtente().getId() == u.getId()) {
			for (Commento c : p.getCommenti()) {
				c.setVisto(1);
			}
		}
		pDao.save(p);
		m.addAttribute("post", p);
		return "ilPost";
	}

	/**
	 * ------------------COMMENTO-------------
	 */

	@PostMapping("/commenta/{postId}")
	public String adcomment(@PathVariable Integer postId, @RequestParam String commento, HttpSession session) {
		Commento comm = new Commento();
		Utente u = (Utente) session.getAttribute("utenteLoggato");
		comm.setCommento(commento);
		comm.setUtente(u);
		comm.setDataCommento(Date.valueOf(LocalDate.now()));
		comm.setPost(pDao.findById(postId).get());
		comm.setVisto(0);
		comm.setDataCommento(Date.valueOf(LocalDate.now()));
		cDao.save(comm);
		return "redirect:/listaPost";

	}

	/**
	 * ------------SETTA RUOLO-------------
	 */

	@GetMapping("/cambia/{utenteId}")
	public String cambiaRuolo(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		if (u.getRuolo().getId() == 3) {
			u.setRuolo(rDao.findById(2).get());
		} else {
			u.setRuolo(rDao.findById(3).get());
		}
		uDao.save(u);

		return "redirect:/listaUtenti";
	}

	@GetMapping("/cambia2/{utenteId}")
	public String cambiaRuolo2(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		if (u.getRuolo().getId() == 3) {
			u.setRuolo(rDao.findById(2).get());
		} else {
			u.setRuolo(rDao.findById(3).get());
		}
		uDao.save(u);

		return "redirect:/profileUser/" + utenteId;
	}

	/**
	 * -----------RIATTIVA L'UTENTE--------------------
	 */

	@PostMapping("/reactive/{utenteId}")
	public String reattiva(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(1);
		u.setFineBan(null);
		uDao.save(u);

		return "redirect:/listaUtenti";
	}

	@PostMapping("/reactive2/{utenteId}")
	public String reattiva2(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(1);
		u.setFineBan(null);
		uDao.save(u);

		return "redirect:/profileUser/" + utenteId;
	}

	/**
	 * 
	 * -----------------BAN---------------
	 */

	@PostMapping("/utenteBan/{utenteId}")
	public String bannaDay(@PathVariable Integer utenteId, @RequestParam Integer giorni) {
		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(0);
		u.setFineBan(System.currentTimeMillis() + giorni * 24 * 60 * 60 * 1000);
		uDao.save(u);

		return "redirect:/listaUtenti";
	}

	@PostMapping("/utenteBan2/{utenteId}")
	public String bannaDay2(@PathVariable Integer utenteId, @RequestParam Integer giorni, Model m) {

		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(0);
		u.setFineBan(System.currentTimeMillis() + giorni * 24 * 60 * 60 * 1000);
		uDao.save(u);

		return "redirect:/profileUser/" + utenteId;
	}

	@PostMapping("/bannaAllTime/{utenteId}")
	public String bannaAllTime(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(0);
		u.setFineBan(null);
		uDao.save(u);

		return "redirect:/listaUtenti";
	}

	@PostMapping("/bannaAllTime2/{utenteId}")
	public String bannaAllTime2(@PathVariable Integer utenteId) {
		Utente u = uDao.findById(utenteId).get();
		u.setAttivo(0);
		uDao.save(u);

		return "redirect:/profileUser/" + utenteId;
	}

	/**
	 * ------------------PROFILO UTENTE------------
	 */
	@GetMapping("/profileUser/{utenteId}")
	public String profiloUtente(@PathVariable Integer utenteId, Model m, HttpSession session) {

		m.addAttribute("utente", uDao.findById(utenteId).get());
		return "profileUser";
	}

	/**
	 * 
	 * -------------------IMMAGINE----------------------
	 */

	@PostMapping("/utenti/save")
	public String saveUser(@RequestParam("image") MultipartFile multipartFile, HttpSession session) throws IOException {
		Utente u = (Utente) session.getAttribute("utenteLoggato");
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		u.setImg("/user-photos/" + u.getId() + "/" + fileName);
		uDao.save(u);
		String uploadDir = "user-photos/" + u.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		return "redirect:/areaPersonale";
	}

}
