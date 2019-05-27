package main.clients;

import java.io.IOException;
import java.util.ArrayList;

import main.model.Task;

public interface IGoogleTaskClient {
	ArrayList<Task> GetAllRegularTasksOfCurrentDay()  throws IOException;
	boolean UpdateTask(String taskName, boolean isCompleted)  throws IOException;
}
