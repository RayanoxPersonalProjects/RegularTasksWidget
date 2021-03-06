package com.rb.android.regularTasksWidget.model.mediation;

import com.rb.android.regularTasksWidget.model.Task;

import java.util.ArrayList;

public class AllTasksResults extends OperationResult {

    private final ArrayList<Task> tasksList;

    public AllTasksResults(ArrayList<Task> tasksSet) {
        super(true, null);

        this.tasksList = tasksSet;
    }

    public AllTasksResults(String errorMessage) {
        super(false, errorMessage);

        this.tasksList = null;
    }



    public ArrayList<Task> getTasksList() {
        return tasksList;
    }
}

