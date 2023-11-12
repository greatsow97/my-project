package com.prog.presentazione.utilis;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailUtilis {

	private static final String ACCOUNT = "sowworker@gmail.com";
	private static final String KEY = "chpdeubbwvjfkgfd";
	
	public static void sendMail(String recipient,String subject,Integer id) {
		
		Properties props = new Properties();
		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ACCOUNT, KEY);
			};
		};
		Session session =Session.getInstance(props,auth);
		Message msg = new MimeMessage(session);
		
		try {
			msg.setSubject(subject);
			msg.setFrom(new InternetAddress(ACCOUNT));
			msg.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent("<a href='http://localhost:4200/cambio/"+id+"'>cambia password</a>","text/html");
		
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
			
			msg.setContent(mp);
			Transport.send(msg);
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void sendMail2(String recipient,String body) {
	
		
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");
		
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ACCOUNT, KEY);
			}
			
		};
		Session session = Session.getInstance(props,auth);
		Message msg = new MimeMessage(session);
		
		try {
			msg.setFrom(new InternetAddress(ACCOUNT));
			msg.setRecipient(RecipientType.TO,new InternetAddress(recipient));
			msg.setSubject("scadenza libro");
			
			msg.setText(body);
			
			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
