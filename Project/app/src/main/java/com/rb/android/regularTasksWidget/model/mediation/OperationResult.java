package com.rb.android.regularTasksWidget.model.mediation;

public class OperationResult {
    private final boolean success;
    private final String errorMessage;

    public OperationResult(boolean isSuccess, String errorMessage) {
        this.success = isSuccess;
        this.errorMessage = errorMessage;
    }

    //Success
    protected OperationResult() {
        this.success = true;
        this.errorMessage = null;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
