package com.opencdk.appwidget.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {

	public Task() {

	}

	public Task(String title, boolean isCompleted) {
		this.setCompleted(isCompleted);
		this.setTitle(title);
	}

	private String title;

	private String date;

	private boolean isCompleted;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCompleted(boolean completed) { isCompleted = completed; }

	public boolean getIsCompleted() {return isCompleted; }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("title", title);
			json.put("date", date);
			json.put("isCompleted", isCompleted);
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		return json;

	}

	public static Task toObject(String jsonString) {
		Task task = null;
		try {
			JSONObject json = new JSONObject(jsonString);

			String title = json.optString("title");
			String date = json.optString("date");
			boolean isCompleted = json.optBoolean("isCompleted");

			task = new Task();
			task.setTitle(title);
			task.setDate(date);
			task.setCompleted(isCompleted);
		} catch (JSONException e) {
			// e.printStackTrace();
		}

		return task;
	}

}
