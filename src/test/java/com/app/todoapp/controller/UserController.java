package com.app.todoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserController {
	
	@Autowired
    private MockMvc mockMvc;
	
	//JUnit + Mockito
	

	

}
