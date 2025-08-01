package com.app.todoapp.dao;

import java.util.List;

import com.app.todoapp.model.TodoTask;

public interface ITaskDao {

public TodoTask saveTask(TodoTask todoTask);
	
	public TodoTask editeTask(TodoTask todoTask);
	
	public int deleteTask(String taskId);
	
	public TodoTask getTaskByTitle(String title);
	
	public TodoTask getTaskByTitleId(String title,int id);
	
	public TodoTask getTaskById(int id);
	
	public List<TodoTask> getTaskList(int startIndex,int maxPageSize);
	
	public List<TodoTask> getSearchTaskByTitle(String title,int startIndex,int maxPageSize);	

	
}
