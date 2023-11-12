package com.prog.presentazione.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.prog.presentazione.Dao.LibroDao;

@Controller
public class ScheduleController {

	@Autowired
	LibroDao lDao;
	
	@Scheduled(cron = "0 30 18 * * *")
	public void noBook() {
	
		lDao.AccessDenied();
		
	}
}
