package main.controllers;

import java.util.ArrayList;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.authentication.ServerTokenProvider;
import main.clients.IGoogleTaskClient;
import main.model.AllTasksResults;
import main.model.OperationResult;
import main.model.SuccessOperation;
import main.model.Task;

@RestController
public class TasksController {

	@Autowired
	ServerTokenProvider serverTokenProvider;
	
	@Autowired
	IGoogleTaskClient googleTaskClient;

    @RequestMapping(value = "/retrieveAllTasks", method = RequestMethod.GET)
    public AllTasksResults RetrieveAllTasks(@RequestParam(value = "token") String token) throws AuthenticationException {
    	OperationResult failedAuth = processAuthorization(token);
    	if(failedAuth != null)
    		return new AllTasksResults(failedAuth.getErrorMessage());
    	
    	ArrayList<Task> allTasksOfCurrentDay;
    	
    	try {
    		allTasksOfCurrentDay = googleTaskClient.GetAllRegularTasksOfCurrentDay();
    	}catch(Exception e) {
    		return new AllTasksResults(e.getMessage() + "\n\n" + e.getStackTrace());
    	}
    	
    	
        return new AllTasksResults(allTasksOfCurrentDay);
    }
    
    @PostMapping(value = "/updateTask")
    public OperationResult UpdateTask(@RequestBody Task task, @RequestParam(value = "token") String token) throws AuthenticationException {
    	OperationResult failedAuth = processAuthorization(token);
    	if(failedAuth != null)
    		return failedAuth;
    	   
    	if(task == null || task.getId() == null || task.getName() == null || task.isCompleted() == null) {
    		return getFailedOperationResult("At least one input argument is null. Retry without null arguments. Task content = \n" + task != null ? task.toString() : "null");
    	}
    	
		OperationResult resultOp = googleTaskClient.UpdateTask(task);

		if (!resultOp.isSuccess())
			return getFailedOperationResult(resultOp.getErrorMessage());
    	
    	return new SuccessOperation();
    }
    
    
    
    
    
    
    
    
    /*
     *  Private tools
     */
    

    
    private OperationResult processAuthorization(String token) throws AuthenticationException {
    	if(!serverTokenProvider.AuthorizeRequestToken(token)) {
    		return new OperationResult(false, "The token does not equal the server-side token.");
    	}else {
    		return null;
    	}
    }
    
    
    private OperationResult getFailedOperationResult(String errorMessage) {
    	return new OperationResult(false, errorMessage);
    }
}
