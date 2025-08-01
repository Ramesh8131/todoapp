package com.app.todoapp.service;

import java.util.List;
import java.util.Map;

import com.app.todoapp.model.Mail;
import com.app.todoapp.model.User;

public interface IUserService {
	
	public User saveUser(User user);
	
	public User findUserById(int id);
	
	public User findUserByEmailId(String email);
	
	public Map<String,Object> sendEmailToAll(Mail emailDetails);
	
}
