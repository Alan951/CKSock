package com.jalan.tests;

import java.io.Serializable;
import java.util.List;

public class TaskList implements Serializable {

	private List<String> taskNames;

	public TaskList(List<String> taskNames) {
		this.taskNames = taskNames;
	}

	public List<String> getTaskNames() {
		return taskNames;
	}

	public void setTaskNames(List<String> taskNames) {
		this.taskNames = taskNames;
	}
	
	
	
}
