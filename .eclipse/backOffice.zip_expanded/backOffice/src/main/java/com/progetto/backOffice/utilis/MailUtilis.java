package com.progetto.backOffice.utilis;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeMessage.RecipientType;

public class MailUtilis {

	private static final String ACCOUNT = "sowworker@gmail.com";
	private static final String KEY = "chpdeubbwvjfkgfd";

	public static void sendEmail(String recipient,String subject,String uid) {
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
		Session session = Session.getInstance(props, auth);
		
		MimeMessage msg = new MimeMessage(session);
		
		try {
			msg.setFrom(new InternetAddress(ACCOUNT));
			msg.setSubject(subject);
			msg.setRecipient(RecipientType.TO, new InternetAddress(recipient));

			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent("<a href='http://localhost:8080/creaPassword/"+uid+"'>cambia</a>","text/html");
			
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
