package com.Esercizio.SocialNet.Utilis;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailUtilis {

	private static final String ACCOUNT = "sowworker@gmail.com";
	private static final String KEY = "chpdeubbwvjfkgfd";

	public static void sendMail(String recipient, String subject, String uid) {

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
		Session session = Session.getInstance(props, auth);

		Message msg = new MimeMessage(session);

		try {
			msg.setSubject(subject);
			msg.setFrom(new InternetAddress(ACCOUNT));
			msg.setRecipient(RecipientType.TO, new InternetAddress(recipient));

			MimeBodyPart bp2 = new MimeBodyPart();
			bp2.setContent("<a href='http://localhost:8080/prova/" + uid + "'> verifica</a>", "text/html");

			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(bp2);

			msg.setContent(mp);
			Transport.send(msg);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void sendMailforP(String recipient,String subject, String uid) {
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
		
		Session session =Session.getInstance(props,auth);
		
		MimeMessage msg = new MimeMessage(session);
		
		try {
			msg.setFrom(new InternetAddress(ACCOUNT));
			msg.setSubject(subject);
			msg.setRecipient(RecipientType.TO,new InternetAddress(recipient));
			
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent("<a href='http://localhost:8080/recuperoPassword/"+uid+"'>crea Password</a>","text/html");
			
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
			
			msg.setContent(mp);
			
			Transport.send(msg);
			
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
