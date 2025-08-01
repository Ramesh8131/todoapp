package com.app.todoapp.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Mail {

	private List<String> to;
	private String subject;
	private String body;
	Map<String, Object> model;

}
