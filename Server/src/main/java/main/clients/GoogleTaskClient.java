package main.clients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.Tasks.Tasklists.Get;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import main.model.OperationResult;
import main.model.SuccessOperation;
import main.model.Task;

@Service
public class GoogleTaskClient implements IGoogleTaskClient {

	private static final String STATUS_TO_DO = "needsAction";
	private static final String STATUS_COMPLETED = "completed";

	private static final String REGULAR_TASKLIST_ID = "MTcwODMyNjEwNTIwOTgwNTU3NzQ6OTU4ODkwOTcwMjY2MzQwMDow";
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

	private static final String APPLICATION_NAME = "RegularTasks-mediation";
	private static final List<String> SCOPES = Collections.singletonList(TasksScopes.TASKS);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";

	private Tasks taskService;

	public GoogleTaskClient() throws GeneralSecurityException, IOException {

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		taskService = new Tasks.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();
	}

	@Override
	public ArrayList<Task> GetAllRegularTasksOfCurrentDay() throws IOException {
		ArrayList<Task> result = new ArrayList<Task>();

		Calendar nextDay = Calendar.getInstance();
		nextDay.add(Calendar.DAY_OF_MONTH, 1);
		
		String currentDayString = getDayDateString(Calendar.getInstance());		
		String nextDayString = getDayDateString(nextDay);
		
		com.google.api.services.tasks.model.Tasks listeDeTasks = taskService.tasks().list(REGULAR_TASKLIST_ID)
				.setDueMin(currentDayString)
				.setDueMax(nextDayString)
				.setShowHidden(true)
				.execute();

		for (com.google.api.services.tasks.model.Task task : listeDeTasks.getItems()) {
			result.add(new Task(task.getId(), task.getTitle(), task.getStatus().equals(STATUS_COMPLETED)));
		}

		return result;
	}

	@Override
	public OperationResult UpdateTask(Task task) {
		try {
			com.google.api.services.tasks.model.Task taskToUpdate = taskService.tasks()
					.get(REGULAR_TASKLIST_ID, task.getId()).execute();

			taskToUpdate.setStatus(task.isCompleted() ? STATUS_COMPLETED : STATUS_TO_DO);

			taskService.tasks().update(REGULAR_TASKLIST_ID, task.getId(), taskToUpdate).execute();
		} catch (Exception e) {
			return new OperationResult(false, e.getLocalizedMessage());
		}

		return new SuccessOperation();
	}

	
	
	
	
	
	/*
	 * Private functions
	 */

	


	private String getDayDateString(Calendar calendar) {
		String patternDate = "%d-%d-%dT00:00:00+00:00";

		return String.format(patternDate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
	}
	
	
	/**
	 * Creates an authorized Credential object.
	 * 
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = GoogleTaskClient.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			String errorMessage = "Please add the '" + CREDENTIALS_FILE_PATH
					+ "' file to the resource folder (src/main/resources)";
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
						.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
						.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

}
