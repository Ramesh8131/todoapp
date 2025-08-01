package com.app.todoapp.config;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import com.app.todoapp.model.LoginDetails;
import com.app.todoapp.security.JwtConfig;
import com.app.todoapp.security.JwtTokenUtil;
import com.app.todoapp.util.AppUtill;

@Component
public class HeadersInterceptors implements HandlerInterceptor {
//	
//	@Autowired
//	CryptoAES128 cryptoAES128;
//	
//	@Autowired
//	Environment environment;
	
	@Autowired
	com.app.todoapp.service.IUserLogOutIn userLogOutIn;
	
	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private JwtTokenUtil JwtTokenUtil;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		try {	
			String header = request.getHeader("Authorization");
			String token = header.substring(7);
			LoginDetails loginDetails=userLogOutIn.fetchLogInUser(Integer.parseInt(JwtTokenUtil.getIdFromToken(token)));
			int c=loginDetails.getApiCount();
			if (c<5) {
				c++;
				loginDetails.setLastApiHitAt(new Timestamp(new Date().getTime()));  
				loginDetails.setApiCount(c);
			}
			userLogOutIn.updateCount(loginDetails);
			System.out.println(c);
			} catch (Exception e) {
	//			e.printStackTrace();
				// TODO: handle exception
			}
//		userLogOutIn.loginUser(Integer.parseInt(JwtTokenUtil.getIdFromToken(token)), JwtTokenUtil.getEmailFromToken(token)).getId());
		
		
//       	System.out.println(request.getRemoteAddr());
       
			return true;
//		}
	}
	
}
