package com.opencdk.appwidget.utils;

import android.content.Context;
import android.os.Looper;

import com.opencdk.appwidget.clients.GoogleTaskMediationClient;
import com.opencdk.appwidget.model.Task;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataProvider {

	// Autorise a recuperer les nouvelles donnees uniquement dans cette plage horaire
	private static final int HOUR_MIN_RETRIEVE_DATAS = 2;
	private static final int HOUR_MAX_RETRIEVE_DATAS = 6;

	private static GoogleTaskMediationClient gtaskClient;

    static {
        try {
            gtaskClient = new GoogleTaskMediationClient();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    //private static boolean firstRetrieve = true;
	private static ArrayList<Task> tasks = new ArrayList<Task>();


    /**
     * Selection de toute la liste de taches à afficher
     *
     * @return
     */
    public static List<Task> getAllTasksOfDay(Context context, boolean firstRetrieve) {

		processWarnIfTokenForTest(context);

		if(isInsideUpdatePeriod() || firstRetrieve) {
			try {
				tasks = new ArrayList<Task>( gtaskClient.GetAllTasksOfCurrentDay() );
				firstRetrieve = false;
			} catch (Exception e) {
				//Toast.makeText(context, "Error when trying to retrieve all the tasks of the day : \n" + e.getMessage(), Toast.LENGTH_LONG);
				e.printStackTrace();
			}
		}

        return tasks;
    }



	/**
	 *  Update une seule tache
	 *
	 * @param task
	 * @param context
	 */
	public static void UpdateTask(Task task, Context context) {
    	try {
			gtaskClient.UpdateTask(task);
			if(!UpdateTaskInDataList(task)) {
				//Toast.makeText(context, "Error when trying to update the task in the data list of widget. For task -> '"+ task.getName() +"'", Toast.LENGTH_LONG);
				System.err.println("Error when trying to update the task in the data list of widget. For task -> '"+ task.getName() +"'");
			}
		}catch (Exception e) {
			System.err.println("Error when trying to update the task '"+ task.getName() +"' : \n" + e.getMessage());
			//Toast.makeText(context, "Error when trying to update the task '"+ task.getName() +"' : \n" + e.getMessage(), Toast.LENGTH_LONG);
		}
	}




	/* ***************

		PRIVATE TOOLS

		***************
	 */



	/**
	 *
	 * @param task
	 * @return true if the operation succeeded, otherwise there is probably a bug due to an update shift of the google sheet trigger
	 */
	private static boolean UpdateTaskInDataList(Task task) {
		for (Task currentTask : tasks ) {
			if(currentTask.getId().equals(task.getId())) {
				currentTask.setCompleted(!task.getIsCompleted());
				return true;
			}
		}
		return false;
	}

    private static boolean isInsideUpdatePeriod() {
		int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		return currentHour >= HOUR_MIN_RETRIEVE_DATAS && currentHour < HOUR_MAX_RETRIEVE_DATAS;
	}

	private static void processWarnIfTokenForTest(Context context) {
		//Looper.prepare();
		if(GoogleTaskMediationClient.TOKEN_SERVER_SIDE.equals("test"))
			System.out.println("\n\nThe TOKEN_SERVER_SIDE's value is set to its default value 'test', please change it \n\n");
			//Toast.makeText(context, "The TOKEN_SERVER_SIDE's value is set to its default value 'test', please change it ", Toast.LENGTH_LONG);
			//NotificationUtil.PublishNotif_Warn("The TOKEN_SERVER_SIDE's value is set to its default value 'test', please change it ", context);
	}

}
