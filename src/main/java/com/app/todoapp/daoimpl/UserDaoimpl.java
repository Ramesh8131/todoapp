package com.app.todoapp.daoimpl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.todoapp.dao.IUserDao;
import com.app.todoapp.model.User;

@Repository
@Transactional
public class UserDaoimpl implements IUserDao {
	
	@Autowired
	EntityManager entityManager;

	@Override
	public User saveUser(User user) {
		return entityManager.merge(user);
	}

	@Override
	public User findUserById(int id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User findUserByEmailId(String email) {
		try {
			User user=(User) entityManager.createQuery("from User where email_id='"+email+"'").getSingleResult();
			return user;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
