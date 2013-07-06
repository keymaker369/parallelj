package com.cometoin;

public final class TaskResult {
	
	public Boolean SUCCESS = false;
	public Long TIMING = new Long(0);

	public TaskResult() {
	}
	
	public TaskResult(Boolean SUCCESS, Long TIMING) {
		this.SUCCESS = SUCCESS;
		this.TIMING = TIMING;
	}

	@Override
	public String toString() {
		return "Result:" + SUCCESS + " " + TIMING + " msecs ";
	}
	
}