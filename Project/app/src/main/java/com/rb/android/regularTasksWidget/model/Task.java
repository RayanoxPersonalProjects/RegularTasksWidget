package com.rb.android.regularTasksWidget.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

	private String id; //Google tasks ID are String type
	private String name;
	private String date;
	private boolean completed;
	private boolean future;
	private int daysFrequence;



	public Task() {

	}

	public Task(String name, boolean completed) {
		this.setCompleted(completed);
		this.setName(name);
	}

	public Task(String id, String name, boolean completed) {
		this(name, completed);
		this.setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCompleted(boolean completed) { this.completed = completed; }

	public void setFuture(boolean future) { this.future = future; }

	public boolean getIsCompleted() {return completed; }

	public int getDaysFrequence() {
		return daysFrequence;
	}

	public void setDaysFrequence(int daysFrequence) {
		this.daysFrequence = daysFrequence;
	}

	public boolean getIsFuture() {return future; }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("name", name);
			json.put("date", date);
			json.put("completed", completed);
			json.put("future", future);
			json.put("daysFrequence", daysFrequence);
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		return json;

	}

	public static Task toObject(String jsonString) {
		Task task = null;
		try {
			JSONObject json = new JSONObject(jsonString);

			String id = json.optString("id");
			String title = json.optString("name");
			String date = json.optString("date");
			boolean isCompleted = json.optBoolean("completed");
			boolean isFuture = json.optBoolean("future");
			int daysFrequence = json.optInt("daysFrequence");

			task = new Task();
			task.setId(id);
			task.setName(title);
			task.setDate(date);
			task.setCompleted(isCompleted);
			task.setFuture(isFuture);
			task.setDaysFrequence(daysFrequence);
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		return task;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
