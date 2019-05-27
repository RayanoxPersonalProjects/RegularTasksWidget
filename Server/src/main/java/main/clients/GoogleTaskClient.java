package main.clients;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
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
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.Tasks.Tasklists.Get;
import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.TaskList;
import com.google.api.services.tasks.model.TaskLists;

import main.model.Task;


@Service
public class GoogleTaskClient implements IGoogleTaskClient {

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
                .setApplicationName(APPLICATION_NAME)
                .build();
	}
	
	
	@Override
	public ArrayList<Task> GetAllRegularTasksOfCurrentDay() throws IOException {
		ArrayList<Task> result = new ArrayList<Task>();
					
		com.google.api.services.tasks.model.Tasks listeDeTasks = taskService.tasks().list(REGULAR_TASKLIST_ID).execute();
		
		for (com.google.api.services.tasks.model.Task task : listeDeTasks.getItems()) {
			result.add(new Task(task.getTitle(), task.getCompleted() != null));
		}
				
		return result;
	}
	
	

	@Override
	public boolean UpdateTask(String taskName, boolean isCompleted) throws IOException {
		
		//TODO implement the update
		
		return false;
	}
	
	
	
	
	
	/*
	 *  Private functions
	 */
	
	
	
	/**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleTaskClient.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
        	String errorMessage = "Please add the '" + CREDENTIALS_FILE_PATH +"' file to the resource folder (src/main/resources)" ;
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
	
	
	
	
	
}
