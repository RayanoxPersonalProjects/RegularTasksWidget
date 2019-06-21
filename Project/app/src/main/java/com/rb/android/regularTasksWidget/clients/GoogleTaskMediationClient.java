package com.rb.android.regularTasksWidget.clients;

import android.content.Context;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.gson.Gson;
import com.rb.android.regularTasksWidget.model.Task;
import com.rb.android.regularTasksWidget.model.mediation.AllTasksResults;
import com.rb.android.regularTasksWidget.model.mediation.OperationResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleTaskMediationClient {

    public static final String TOKEN_SERVER_SIDE = "test";

    public static final String defaultIP = "10.12.0.87";

    public static final String URL_RETRIEVE_ALL_TASKS_OF_DAY = "https://10.12.0.87:1943/retrieveAllTasks";
    public static String URL_RETRIEVE_ALL_FUTURE_TASKS = "https://10.12.0.87:1943/retrieveAllFutureTasks";
    public static String URL_UPDATE_ONE_TASK = "https://10.12.0.87:1943/updateTask";




    private HttpRequestFactory requestFactory;

    public GoogleTaskMediationClient() throws GeneralSecurityException {
        //requestFactory = new NetHttpTransport().createRequestFactory();
        NetHttpTransport.Builder builder = new NetHttpTransport.Builder();
        requestFactory = builder
                .doNotValidateCertificate() // Not matter because very low chance to undergo a man-in-the-middle attack cause I always use the phone network and no WIFI.
                .build().createRequestFactory();
    }

    public List<Task> GetAllTasksOfCurrentDay() throws Exception {
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(addTokenToUrl(URL_RETRIEVE_ALL_TASKS_OF_DAY)));


        HttpResponse response = request.execute();

        AllTasksResults result = convertJsonToAllTasksResults(response.parseAsString());

        if(!result.isSuccess())
            throw new Exception("The WS returned an unsuccessed response --> " + result.getErrorMessage());

        return result.getTasksList();
    }

    public List<Task> GetAllFutureTasks() throws Exception {
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(addTokenToUrl(URL_RETRIEVE_ALL_FUTURE_TASKS)));


        HttpResponse response = request.execute();

        AllTasksResults result = convertJsonToAllTasksResults(response.parseAsString());

        if(!result.isSuccess())
            throw new Exception("The WS returned an unsuccessed response --> " + result.getErrorMessage());

        return result.getTasksList();
    }


    public void UpdateTask(Task task) throws Exception {
        String requestJsonBody = convertTaskToJsonString(task);

        HttpContent content = ByteArrayContent.fromString("application/json", requestJsonBody);
        HttpRequest request = requestFactory.buildPostRequest(
                new GenericUrl(addTokenToUrl(URL_UPDATE_ONE_TASK)), content);

        request.getHeaders().setContentType("application/json");

        // Sends the request and get the response

        HttpResponse response = request.execute();

        OperationResult result = convertJsonToAllTasksResults(response.parseAsString());

        if(!result.isSuccess())
            throw new Exception("The WS returned an unsuccessed response --> " + result.getErrorMessage());


        //System.out.println("Update de la task " + task.getName() + ", ID = " + idTask);
    }



    /*
        Privates functions
     */


    private String addTokenToUrl(String url) {
        char separator = url.contains("?") ? '&' : '?';
        return url + separator + "token=" + TOKEN_SERVER_SIDE;
    }

    private AllTasksResults convertJsonToAllTasksResults(String jsonText) {
        Gson converter = new Gson();
        AllTasksResults result = converter.fromJson(jsonText, AllTasksResults.class);
        return result;
    }

    private String convertTaskToJsonString(Task task) {
        Gson converter = new Gson();
        String result = converter.toJson(task);
        return result;
    }

    //private String
}
