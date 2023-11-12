package com.Esercizio.SocialNet.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.Esercizio.SocialNet.Dao.UtenteDao;

@Controller
public class ScheduleController {

	@Autowired
	UtenteDao uDao;
	
	@Scheduled(cron = "0 0 18 * * *")
	public void reactivateUsers() {
		
	uDao.reactiveUsers(System.currentTimeMillis());
	}
	
}
