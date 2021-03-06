package main.model;

public class OperationResult {
	
	private final boolean isSuccess;
	private final String errorMessage;
	
	public OperationResult(boolean isSuccess, String errorMessage) {
		this.isSuccess = isSuccess;
		this.errorMessage = errorMessage;
	}
	
	//Success
	protected OperationResult() {
		this.isSuccess = true;
		this.errorMessage = null;		
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
