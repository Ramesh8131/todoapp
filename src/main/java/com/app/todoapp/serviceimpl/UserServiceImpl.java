package com.app.todoapp.serviceimpl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.app.todoapp.dao.IUserDao;
import com.app.todoapp.model.Mail;
import com.app.todoapp.model.User;
import com.app.todoapp.service.IUserService;

import lombok.experimental.StandardException;

@Service
public class UserServiceImpl implements IUserService {

	private volatile String em = null;
	
	Map<String,Object> smail=null;
	
	
	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private IUserDao userDao;

	@Override
	public User saveUser(User user) {
		return userDao.saveUser(user);
	}

	@Override
	public User findUserById(int id) {
		return userDao.findUserById(id);
	}

	@Override
	public User findUserByEmailId(String email) {
		return userDao.findUserByEmailId(email);
	}

	@Override
	public Map<String,Object> sendEmailToAll(Mail emailDetails) {
		smail=Collections.synchronizedMap(new HashMap<>());
		List<String> invalidEmail=new CopyOnWriteArrayList<>();
		List<String> validEmail=new CopyOnWriteArrayList<>();
		emailDetails.getTo().stream().forEach(toEmail -> {
			CompletableFuture.runAsync(() -> {
				try {
                    em=toEmail;
                   /// Mail mail = new ObjectMapper.readValue(jsonStr, Mail.class);
					Map<String, Object> data = new HashMap<>();

					MimeMessage message = emailSender.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(message,
							MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//					Context context = new Context();
//					// context.setVariables(mail.getModel());
//					String html = templateEngine.process(
//							"path for html page in templates  or fontend folder or html page name in templates folder ",
//							context);

					helper.setTo(toEmail);
					helper.setText(emailDetails.getBody(), true);
					helper.setSubject(emailDetails.getSubject());
					helper.setFrom("rameshkumar13111@gmail.com");
					emailSender.send(message);
				
//					emailData(toEmail,1);
					validEmail.add(em);
				} catch (MessagingException e) {
					
//					emailData(toEmail,2);
					e.printStackTrace();
				}catch (Exception e2) {
					invalidEmail.add(em);
					System.out.println("aaaaaaaaaaaaaaaaaaaa");
					e2.printStackTrace();
					// TODO: handle exception
				}
				smail.put("emailSend", validEmail);
				smail.put("invalidEmail", invalidEmail);
				
			});
			System.out.println(smail);
		});
		try {
			Thread.sleep(3000*emailDetails.getTo().stream().count());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return smail;
	}
	
	private void emailData(String e,int status) {
		if (status==1) {
			smail.put("emailSend", e);
		} else {
			smail.put("invalidEmail", e);
		}
		System.out.println(smail);
		
	}

}
