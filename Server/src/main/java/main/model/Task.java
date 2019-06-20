package main.model;

import java.util.Calendar;
import java.util.Date;

public class Task {

	private final String id; //Ids are String with Google
	private String name;
	private Boolean isCompleted;
	
	private Date date;
	private Boolean isFuture;
	private int daysFrequence;
	
	public Task(String id, String name, boolean isCompleted) {
		this.name = name;
		this.setCompleted(isCompleted);
		this.id = id;
	}
	
	public Task(String id, String name, boolean isCompleted, Date date, int daysFrequence) {
		this(id, name, isCompleted);
		this.date = date;
		this.isFuture = isTaskFuture();
		this.daysFrequence = daysFrequence;
	}
	
	
	private boolean isTaskFuture() {
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.date);
		return cal.after(today) 
			&& (    (cal.get(Calendar.DAY_OF_YEAR) != today.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == today.get(Calendar.YEAR))
				 || (cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) != today.get(Calendar.YEAR)));
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
	
	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}
	
	public Boolean isFuture() {
		return isFuture;
	}

	public Date getDate() {
		return date;
	}
	
	public String toString() {
		return "id = " + id + "\n"
				+ "name = " + name + "\n"
				+ "isCompleted = " + isCompleted;
	}


	public int getDaysFrequence() {
		return daysFrequence;
	}


}
