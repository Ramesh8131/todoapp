package com.app.todoapp.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CacheConcurrencyStrategy;
//import org.springframework.scheduling.config.Task;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Table(name="user")
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7016888389920150869L;


	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Name is mandatory")
	private String name;
	
	@Email
	@NotBlank(message = "Email is mandatory")
	private String email_id;
	
	@Pattern(regexp = "^[0-9]{10,10}$",message="Enter valid mobile number") 
	private String mobile_no;
	
	@Pattern(regexp = "^[0-9]{1,3}$",message="Enter valid contry code") 
	private String country_code;
	
	@NotBlank(message = "Password is mandatory")
	private String password;
	
	private int status;// 1-active,2-InActive,3-delete
	private Timestamp created_at;
	private String role;// 1- admin,2-user
	
	private String gender;
	private Date lastPasswordResetDate;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.PERSIST,fetch=FetchType.LAZY)
	Set<TodoTask> todoTask=new HashSet<>();
	
	public static PasswordEncoder getPasswordEncoder() {
		return PASSWORD_ENCODER;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getEmail_id() {
		return email_id;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public String getCountry_code() {
		return country_code;
	}
	public String getPassword() {
		return password;
	}
	public int getStatus() {
		return status;
	}
	public Timestamp getCreated_at() {
		return created_at;
	}
	public String getRole() {
		return role;
	}
	public String getGender() {
		return gender;
	}
	public Date getLastPasswordResetDate() {
		return lastPasswordResetDate;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setLastPasswordResetDate(Date lastPasswordResetDate) {
		this.lastPasswordResetDate = lastPasswordResetDate;
	}
	
	
	
}
