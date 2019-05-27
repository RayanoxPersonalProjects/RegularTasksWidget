package main.controllers;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import main.Authentication.ServerTokenProvider;
import model.AllTasksResults;
import model.OperationResult;

@RestController
public class TasksController {

	@Autowired
	ServerTokenProvider serverTokenProvider;

    @RequestMapping(value = "/retrieveAllTasks", method = RequestMethod.GET)
    public AllTasksResults RetrieveAllTasks(@RequestParam(value = "token") String token) throws AuthenticationException {
    	OperationResult failedAuth = processAuthorization(token);
    	if(failedAuth != null)
    		return new AllTasksResults(failedAuth.getErrorMessage());
    	
    	String [] allTasksOfCurrentDay;
    	
    	try {
    		allTasksOfCurrentDay = new String [] {"Faire le ménage","Faire les courses","Ne rien foutre et manger du Nutella"};
    	}catch(Exception e) {
    		return new AllTasksResults(e.getMessage() + "\n\n" + e.getStackTrace());
    	}
    	
    	
        return new AllTasksResults(allTasksOfCurrentDay);
    }
    
    public OperationResult UpdateTasks(@RequestParam(value = "taskTitle") String taskTitle, @RequestParam(value = "isCompleted") Boolean isCompleted, @RequestParam(value = "token") String token) throws AuthenticationException {
    	OperationResult failedAuth = processAuthorization(token);
    	if(failedAuth != null)
    		return failedAuth;
    	
    	
    	if(taskTitle == null || isCompleted == null) {
    		return getFailedOperationResult("At least one of the input parameters is null"); 
    	}
    	
    	
    	boolean isOperationSuccess;
    	try {
    		// On met à jour les tasks
    		isOperationSuccess = true;
    	}catch(Exception e) {
    		return getFailedOperationResult(e.getMessage() + "\n\n" + e.getStackTrace());
    	}
    	
    	return new OperationResult(isOperationSuccess, null);
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
