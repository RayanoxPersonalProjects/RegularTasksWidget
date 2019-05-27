package main.model;

public class Task {

	private final String id; //Ids are String with Google
	private final String name;
	private Boolean isCompleted;
	
	public Task(String id, String name, boolean isCompleted) {
		this.name = name;
		this.setCompleted(isCompleted);
		this.id = id;
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


	public String getId() {
		return id;
	}
	
	public String toString() {
		return "id = " + id + "\n"
				+ "name = " + name + "\n"
				+ "isCompleted = " + isCompleted;
	}
}
