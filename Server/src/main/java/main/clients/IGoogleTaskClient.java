package main.clients;

import java.io.IOException;
import java.util.ArrayList;

import main.model.OperationResult;
import main.model.Task;

public interface IGoogleTaskClient {
	ArrayList<Task> GetAllRegularTasksOfCurrentDay()  throws IOException;
	OperationResult UpdateTask(Task task);
}
