package main.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.AllTasksResults;
import model.OperationResult;

@RestController
public class TasksController {


    @RequestMapping(value = "/retrieveAllTasks", method = RequestMethod.GET)
    public AllTasksResults RetrieveAllTasks(@RequestParam(value="name", defaultValue="World") String name) {
    	
    	String [] allTasksOfCurrentDay;
    	
    	try {
    		allTasksOfCurrentDay = new String [] {"Faire le ménage","Faire les courses","Ne rien foutre et manger du Nutella"};
    	}catch(Exception e) {
    		return new AllTasksResults(e.getMessage() + "\n\n" + e.getStackTrace());
    	}
    	
    	
        return new AllTasksResults(allTasksOfCurrentDay);
    }
    
    public OperationResult UpdateTasks(@RequestParam(value = "taskTitle") String taskTitle, @RequestParam(value = "isCompleted") Boolean isCompleted) {
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
    
    
    private OperationResult getFailedOperationResult(String errorMessage) {
    	return new OperationResult(false, errorMessage);
    }
}
