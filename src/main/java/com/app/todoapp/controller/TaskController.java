package com.app.todoapp.controller;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.todoapp.annotation.ApiSecured;
import com.app.todoapp.annotation.Base64Encoded;
import com.app.todoapp.annotation.UserCache;
import com.app.todoapp.config.UIBean;
import com.app.todoapp.exception.BadGatewayRequest;
import com.app.todoapp.exception.TaskTitleException;
import com.app.todoapp.exception.TaskTitleNotFound;
import com.app.todoapp.model.TodoTask;
import com.app.todoapp.model.User;
import com.app.todoapp.model.dto.TaskDto;
import com.app.todoapp.security.JwtTokenUtil;
import com.app.todoapp.service.ITaskService;
import com.app.todoapp.service.IUserLogOutIn;
import com.app.todoapp.service.IUserService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/api/todoTask/")
@ApiSecured
@Api(value="",tags= {"TodoApp Api"})
public class TaskController {
	
	Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private Environment environment;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private com.app.todoapp.security.SecurityService securityService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private ModelMapper modelMapper;
		
	
//	private final Bucket bucket;

//    public TaskController() {
//        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
//        this.bucket = Bucket4j.builder()
//                .addLimit(limit)
//                .build();
//    }
//    
	 @RequestMapping(value = "test",method = RequestMethod.GET)
		public ResponseEntity<Map<String,Object>> test(@RequestBody @Base64Encoded String user,HttpServletRequest request,@RequestParam("searchText") String searchText){

		 //5L2g5aW9IFNwcmluZw==
		 
		 //		 UIBean<ResponseEntity<Map<String,Object>>>
//		 UIBean<ResponseEntity<Map<String,Object>>> d=new UIBean<>();
		Map<String,Object> response=new HashMap<>();
//		 d.setResponseBody(taskService.getTaskById(5));
//		InetSocketAddress socketAddress = (InetSocketAddress) connectedSocket.getRemoteSocketAddress();
		System.out.println("Authorization------------dd---------------"+securityService.username());
		
		 response.put("aa", user);
		 response.put("searchText", searchText);
//		 System.out.println(response);
		 return new ResponseEntity<>(response, HttpStatus.OK);
	 }
	
	 @RequestMapping(value = "saveTask",method = RequestMethod.POST)
		public ResponseEntity<Map<String,Object>> saveTask(@RequestBody(required=true)  @Valid TodoTask todoTask,BindingResult result,HttpServletRequest request){
			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			System.out.println(todoTask);
			    if (todoTask == null) {
			    	throw new BadGatewayRequest("Invalid Request");
				} else if (result.hasFieldErrors()) { 
					throw new BadGatewayRequest(result.getFieldError().getDefaultMessage());
				}else{
					TodoTask checkTask=taskService.getTaskByTitle(todoTask.getTitle());
					String email=jwtTokenUtil.getEmailFromToken(request.getHeader(environment.getProperty("security.jwt.header")).substring(7));
					User user=userService.findUserByEmailId(email);
					if (user == null) {
						throw new BadGatewayRequest("Invalid Token");
					}else if (checkTask == null) {
						todoTask.setUser(user);
						todoTask.setStatus(1);
						todoTask.setCreated_at(new Timestamp(new Date().getTime()));
						System.out.println(todoTask);
						todoTask = taskService.saveTask(todoTask);
						TaskDto  taskDto = modelMapper.map(todoTask, TaskDto.class);
						data.put("user", taskDto);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","TodoTask has been created successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						throw new TaskTitleException("TodoTask Title already registered with us.");
					}
				}
	    }
	 
	 @RequestMapping(value = "editTask",method = RequestMethod.PATCH)
		public ResponseEntity<Map<String,Object>> editTask(@RequestBody @Valid TodoTask todoTask,BindingResult result,HttpServletRequest request){
			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			    if (todoTask == null) {
					throw new BadGatewayRequest("Invalid Request");
				}else if (result.hasFieldErrors()) { 
					throw new BadGatewayRequest(result.getFieldError().getDefaultMessage());
				}else{
					TodoTask checkTask=taskService.getTaskById(todoTask.getId());
					String email=jwtTokenUtil.getEmailFromToken(request.getHeader(environment.getProperty("security.jwt.header")).substring(7));
					User user=userService.findUserByEmailId(email);
				
					if (checkTask != null) {
						checkTask.setStatus((todoTask.getStatus()!=0)?(todoTask.getStatus()):1);
						checkTask.setUpdated_at(new Timestamp(new Date().getTime()));
						checkTask.setTask_description(todoTask.getTask_description());
						checkTask.setUser(user);
						TodoTask checkTask1=taskService.getTaskByTitleId(todoTask.getTitle(),todoTask.getId());
						if(checkTask1==null) {
							todoTask = taskService.editeTask(user.getId(),checkTask);
						}else {
							throw new TaskTitleException("Title already existing");
						}
						TaskDto  taskDto = modelMapper.map(todoTask, TaskDto.class);
						data.put("user", taskDto);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","TodoTask has been created successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						throw new TaskTitleNotFound("TodoTask ID Not Found");
					}
				}
	    }
	 
	 @RequestMapping(value = "{taskIds}/deleteTask",method = RequestMethod.DELETE)
		public ResponseEntity<Map<String,Object>> deleteTask(@PathVariable String[] taskIds){
			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			    if (taskIds.length != 0) {
			    	//exception alreday handled globaly if wrong id
					    taskService.deleteTask(taskIds);
						data.put("taskId", taskIds);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","Tasks has been deleted successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						throw new TaskTitleNotFound("TodoTask ID Not Found");
					}				
	    }
	 
	    @RequestMapping(value = "searchTask",method = RequestMethod.GET)
		@UserCache
		public ResponseEntity<Map<String,Object>> searchTask(@RequestParam("searchText") String searchText,
				@RequestParam("startIndex") int startIndex,
				@RequestParam("maxPageSize") int maxPageSize,HttpServletRequest request){
			Map<String,Object> response=new HashMap<>();
			String userId=jwtTokenUtil.getIdFromToken(request.getHeader(environment.getProperty("security.jwt.header")).substring(7));
			System.out.println("Authorization------------dd---------------"+securityService.username());
			
//			System.out.println(userId);
//			<cacheLoaderFactory class="net.sf.ehcache.constructs.refreshahead.StringifyCacheLoaderFactory" />
//			net.sf.ehcache.constructs.refreshahead.   
			Map<String,Object> data=new HashMap<>();
			    if (searchText.length() != 0&&startIndex>-1&&maxPageSize>1) {
//			    	net.sf.ehcache.constructs.refreshahead.RefreshAheadCacheFactory
			    	//exception alreday handled globaly if wrong id
					   List<TodoTask> taskList=taskService.searchTaskByTitle(Integer.parseInt(userId),searchText, startIndex, maxPageSize);
					   if (taskList.size()>0) {
					    data.put("taskListSize", taskList.size());
					    data.put("taskList", taskList);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","Tasks has been get successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
						}else {
							throw new TaskTitleNotFound("TodoTask Not Found");
						}
					} else {
						throw new TaskTitleNotFound("Page index and page record size are wrong entered");
					}				
	    }
	 


	 @RequestMapping(value = "taskList",method = RequestMethod.GET)
	 @UserCache
		public ResponseEntity<Map<String,Object>> taskList(
				@RequestParam("startIndex") int startIndex,
				@RequestParam("maxPageSize") int maxPageSize,HttpServletRequest request){
			String userId=jwtTokenUtil.getIdFromToken(request.getHeader(environment.getProperty("security.jwt.header")).substring(7));

			Map<String,Object> response=new HashMap<>();
			Map<String,Object> data=new HashMap<>();
			    if (startIndex>-1&&maxPageSize>1) {
			    	//exception alreday handled globaly if wrong id
					    List<TodoTask> taskList=taskService.taskList(Integer.parseInt(userId),startIndex, maxPageSize);
//					    List<TaskDto>  taskDto =  modelMapper.map(taskList, List.class);
						
					    if (taskList.size()>0) {							  
					    data.put("taskListSize", taskList.size());
					    data.put("taskList", taskList);
						response.put("data", data);
						response.put("status", "OK");
						response.put("code", "200");
						response.put("message","Tasks has been get successfully.");
						return new ResponseEntity<>(response, HttpStatus.OK);
					    }else {
							throw new TaskTitleNotFound("TodoTask Not Found");
						}
					} else {
						throw new TaskTitleNotFound("Page index and page record size are wrong entered");
					}				
	    }
}
