package com.cometoin;

public final class TaskResult {
	
	public Boolean SUCCESS = false;
	public Long TIMING = new Long(0);

	@Override
	public String toString() {
		return "Result:" + SUCCESS + " " + TIMING + " msecs ";
	}
	
}