package main.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import lombok.Data;

@Service
@Data
public class Mail {
	
		@Autowired
	   private MailSender mailSender;  
		private int code;
	   
	    public void setMailSender(MailSender mailSender) {  
	        this.mailSender = mailSender;  
	    }  
	   
	    public void sendMail(String from, String to, String subject, String msg) {  
	        //creating message  
	        SimpleMailMessage message = new SimpleMailMessage();  
	        message.setFrom(from);  
	        message.setTo(to);  
	        message.setSubject(subject);  
	        message.setText(msg);  
	        //sending message  
	        mailSender.send(message); 
	    }  
	    public int sendMail(String to) {  
	    	Random random = new Random();
	    	code=random.nextInt(99999)+99999;
	        //creating message  
	        SimpleMailMessage message = new SimpleMailMessage();  
	        message.setFrom("SpendingTracker");  
	        message.setTo(to);  
	        message.setSubject("Reset Password");  
	        message.setText("Your verification code is: "+code);  
	        //sending message  
	        mailSender.send(message); 
	        return code;
	    } 

}
