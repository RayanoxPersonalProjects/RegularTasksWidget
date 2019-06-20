package main.clients;

import java.io.IOException;
import java.util.ArrayList;

import main.exceptions.BadFormatPropertyException;
import main.model.OperationResult;
import main.model.Task;

public interface IGoogleTaskClient {
	ArrayList<Task> GetAllRegularTasksOfCurrentDay()  throws IOException;
	ArrayList<Task> GetAllRegularTasksOfFutureDays() throws IOException, BadFormatPropertyException;
	OperationResult UpdateTask(Task task);
}
