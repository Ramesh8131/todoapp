package com.app.todoapp.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.app.todoapp.dao.ITaskDao;
import com.app.todoapp.model.TodoTask;
import com.app.todoapp.model.User;
import com.app.todoapp.service.ITaskService;

@Service
@CacheConfig(cacheNames = "userCache", keyGenerator = "TransferKeyGenerator")
public class TaskServiceImpl implements ITaskService{

	@Autowired
	ITaskDao taskDao;
	
	@Override
	@CacheEvict //("userCache")
	public TodoTask saveTask(TodoTask todoTask) {
		return taskDao.saveTask(todoTask);
	}

	@Override
	@CacheEvict//it clears the cache id wise
//	@Caching( put = {
//	        @CachePut(key = "#id"),
//	        @CachePut(key = "#todoTask.getId()")
//	})
	public TodoTask editeTask(int id,TodoTask todoTask) {
		return taskDao.editeTask(todoTask);
	}

	@Override
	@CacheEvict
	public int deleteTask(String... taskId) {
		StringBuilder ids=new StringBuilder();
		for (int i = 0; i < taskId.length; i++) {
			if (i==0) {
				ids.append(taskId[i]);
			}else {
				ids.append(","+taskId[i]);
			}
			
		}
		System.out.println(ids);
		return taskDao.deleteTask(ids.toString());
	}

	@Override
	public TodoTask getTaskByTitle(String title) {
		return taskDao.getTaskByTitle(title);
	}

	@Override
	public TodoTask getTaskById(int id) {
		return taskDao.getTaskById(id);
	}

	@Override
	@Cacheable //(value="userCache", key="#user.id")
	public List<TodoTask> taskList(int id,int startIndex, int maxPageSize) {
		startIndex=getPaginationIndex(startIndex, maxPageSize);
//System.out.println(taskDao.getTaskList(startIndex, maxPageSize).get(0).getTask_description());
		return taskDao.getTaskList(startIndex, maxPageSize);
	}

	@Override
	@Cacheable //("userCache")
	public List<TodoTask> searchTaskByTitle(int id,String title, int startIndex, int maxPageSize) {
		startIndex=getPaginationIndex(startIndex, maxPageSize);
		return taskDao.getSearchTaskByTitle(title, startIndex, maxPageSize);
	}
	
	public int getPaginationIndex(int startIndex,int maxPageSize) {
	     startIndex=startIndex==0?startIndex:(startIndex*maxPageSize);	
		return startIndex;
	}

	@Override
	public TodoTask getTaskByTitleId(String title,int id) {
		return taskDao.getTaskByTitleId(title, id);
	}

	

	

}
