package model;

public class AllTasksResults extends OperationResult {

    private final String [] tasksList;

    public AllTasksResults(String [] tasksSet) {
    	super(true, null);
    	
        this.tasksList = tasksSet;
    }

    public AllTasksResults(String errorMessage) {
    	super(false, errorMessage);
    	
        this.tasksList = null;
    }
    
    

    public String [] getTasksList() {
        return tasksList;
    }
}
