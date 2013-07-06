package com.cometoin;

public class TasksExecutorResult {

	public Boolean SUCCESS; 
	
	public long secconds;

	public TasksExecutorResult(Boolean SUCCESS, long secconds) {
		this.SUCCESS = SUCCESS;
		this.secconds = secconds;
	}

	@Override
	public String toString() {
		return "TasksExecutorResult [SUCCESS=" + SUCCESS + ", secconds=" + secconds + "]";
	}
	
}
