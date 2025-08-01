package com.app.todoapp.model.dto;

import java.sql.Date;
import java.sql.Timestamp;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
	
	private    int id;
	private    String name;
	private    String email_id;
	private    String mobile_no;
	private    String country_code;
	private    int status;// 1-active,2-InActive,3-delete
	private    Timestamp created_at;
	private    String role;// 1- admin,2-user
	private    String gender;
	private    Date lastPasswordResetDate;
	
}
