package main.model;

public class Task {

	private final String name;
	private Boolean isCompleted;
	
	public Task(String name, boolean isCompleted) {
		this.name = name;
		this.setCompleted(isCompleted);
	}
	

	public String getName() {
		return name;
	}

	public Boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
}
