package com.app.todoapp.dao;

import com.app.todoapp.model.User;

public interface IUserDao {
	
    public User saveUser(User user);
	
	public User findUserById(int id);
	
	public User findUserByEmailId(String email);


}
