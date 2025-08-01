package com.app.todoapp.service;

import java.util.List;

import com.app.todoapp.model.TodoTask;
import com.app.todoapp.model.User;

public interface ITaskService {

	public TodoTask saveTask(TodoTask todoTask);
	
	public TodoTask editeTask(int id,TodoTask todoTask);
	
	public int deleteTask(String... taskId);
	
	public TodoTask getTaskByTitle(String title);
	
	public TodoTask getTaskByTitleId(String title,int id);
	
	public TodoTask getTaskById(int id);
	
	public List<TodoTask> taskList(int id,int startIndex,int maxPageSize);
	
	public List<TodoTask> searchTaskByTitle(int id,String title,int startIndex,int maxPageSize);	
	
}
