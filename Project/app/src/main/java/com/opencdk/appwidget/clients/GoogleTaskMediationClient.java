package com.opencdk.appwidget.clients;

import android.content.Context;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gson.Gson;
import com.opencdk.appwidget.model.Task;
import com.opencdk.appwidget.model.mediation.AllTasksResults;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleTaskMediationClient {

    public static final String TOKEN_SERVER_SIDE = "test";

    private final String URL_RETRIEVE_ALL_TASKS_OF_DAY = "https://10.12.0.87:1943/retrieveAllTasks";
    private final String URL_UPDATE_ONE_TASK = "https://10.12.0.87:1943/updateTask";

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


    public void UpdateTask(Task task) throws NotImplementedException {
        String url = addTokenToUrl(URL_UPDATE_ONE_TASK);
        throw new NotImplementedException();
    }



    public class NotImplementedException extends Exception {
        public NotImplementedException() {
            super("The method has not been implemented yet");
        }
    }




    private String addTokenToUrl(String url) {
        char separator = url.contains("?") ? '&' : '?';
        return url + separator + "token=" + TOKEN_SERVER_SIDE;
    }

    private AllTasksResults convertJsonToAllTasksResults(String jsonText) {
        Gson converter = new Gson();
        AllTasksResults result = converter.fromJson(jsonText, AllTasksResults.class);
        return result;
    }

    //private String
}
