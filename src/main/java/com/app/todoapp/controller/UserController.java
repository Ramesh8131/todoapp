package com.app.todoapp.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.todoapp.annotation.ApiSecured;
import com.app.todoapp.exception.BadGatewayRequest;
import com.app.todoapp.exception.TaskTitleException;
import com.app.todoapp.exception.TaskTitleNotFound;
import com.app.todoapp.model.LoginDetails;
import com.app.todoapp.model.Mail;
import com.app.todoapp.model.User;
import com.app.todoapp.model.dto.UserDto;
import com.app.todoapp.security.JwtTokenUtil;
import com.app.todoapp.security.JwtUser;
import com.app.todoapp.service.IUserLogOutIn;
import com.app.todoapp.service.IUserService;
import com.app.todoapp.util.CryptoAES128;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/api/user/")
@ApiSecured
@Api(value="",tags= {"User Api"})
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	CryptoAES128 cryptoAES128;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	

	@Autowired
	private IUserLogOutIn userLogOutIn;
	
	@Autowired
	private Environment environment;
	
	 @RequestMapping(value = "sendEmail",method = RequestMethod.POST)
		public ResponseEntity<Map<String,Object>> sendEmail(@RequestBody(required=true) @Valid Mail mail,BindingResult result,HttpServletRequest request){
		     Map<String,Object> response=new HashMap<>();
			    if (mail == null) {
				   	throw new BadGatewayRequest("Invalid Request");
				} else if (result.hasFieldErrors()) {
					throw new BadGatewayRequest(result.getFieldError().getDefaultMessage());
				} else {
					Map<String,Object> data =  userService.sendEmailToAll(mail);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","Your account has been created successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);				
				}
	    }
	
	 @RequestMapping(value = "signup",method = RequestMethod.POST)
		public ResponseEntity<Map<String,Object>> signup(@RequestBody(required=true) @Valid User user,BindingResult result,HttpServletRequest request){
		     Map<String,Object> response=new HashMap<>();
			 Map<String,Object> data=new HashMap<>();
			
				if (user == null) {
				   	throw new BadGatewayRequest("Invalid Request");
				} else if (result.hasFieldErrors()) {
					throw new BadGatewayRequest(result.getFieldError().getDefaultMessage());
				} else {
					User user1 = userService.findUserByEmailId(user.getEmail_id());
					if (user1 == null) {
						user.setStatus(1);
						user.setPassword(User.PASSWORD_ENCODER.encode(user.getPassword()));
						user.setCreated_at(new Timestamp(new Date().getTime()));
						user.setRole("user");
						user = userService.saveUser(user);
						UserDto  userDto = modelMapper.map(user, UserDto.class);
						final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(user.getEmail_id());
						final String token = jwtTokenUtil.generateToken(userDetails,request);					
						data.put("access_token", token);
						data.put("user", userDto);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","Your account has been created successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						throw new TaskTitleException("Email id already registered with us.");
					}
				}
	    }
	    
	    @RequestMapping(value = "login",method = RequestMethod.POST)
		public ResponseEntity<Map<String,Object>> login(@RequestParam("username") String username,@RequestParam("password") String password,HttpServletRequest request){
			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			User user = userService.findUserByEmailId(username);
			System.out.println(username);
			
			    if (user == null) {
			    	throw new TaskTitleNotFound("Email id not existing with us.");
				} else if(User.PASSWORD_ENCODER.matches(password,user.getPassword())){
//					   LoginDetails login=new LoginDetails();
//			    	   login.setUsername(username);
//			    	   login.setId(user.getId());
//			    	   login.setApiCount(1);
//			    	   login.setInSecApiHit(30);
//			    	   login.setLastApiHitAt(new Timestamp(new Date().getTime()));
//			    	   login.setLoginAt(new Timestamp(new Date().getTime()));
//			    	   login.setRequestIp(request.getRemoteAddr());
//					
//					userLogOutIn.loginUser(login);
					
					UserDto userDto = modelMapper.map(user, UserDto.class);
					final JwtUser userDetails = (JwtUser) userDetailsService.loadUserByUsername(user.getEmail_id());
					final String token = jwtTokenUtil.generateToken(userDetails,request);
					data.put("access_token",cryptoAES128.encrypt(token));
//					data.put("access_token",token);
					data.put("user", userDto);
//					response.put("dataff", userLogOutIn.fetchLogInUser(user.getId(), user.getEmail_id()));
					
					response.put("data", data);
					response.put("status", "OK");
					response.put("code", "200");
					response.put("message","Your account has been logined successfully.");
					return new ResponseEntity<>(response, HttpStatus.OK);
				}else {
					throw new TaskTitleException("Wrong password.");
				}	
	    }
	    
	    @RequestMapping(value = "logOut",method = RequestMethod.POST)
		public ResponseEntity<Map<String,Object>> logOut(HttpServletRequest request){
			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			String email=jwtTokenUtil.getEmailFromToken(request.getHeader(environment.getProperty("security.jwt.header")).substring(7));
			User user=userService.findUserByEmailId(email);
			     	userLogOutIn.logOutUser(user.getId(), user.getEmail_id());		
					response.put("data", data);
					response.put("status", "OK");
					response.put("code", "200");
					response.put("message","Your account has been logout successfully.");
					return new ResponseEntity<>(response, HttpStatus.OK);
			
	    }
	   
	    @RequestMapping(value = "testapi",method = RequestMethod.GET)
        public String d() {
	    	return "abc";
	    }
	    @RequestMapping(value = "testapi2",method = RequestMethod.GET)
        public String d2() {
	    	return "abc";
	    }
}
