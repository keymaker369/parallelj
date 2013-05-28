package com.cometoin;

public final class TaskResult {
	
	public Boolean SUCCESS;
	public Long TIMING;

	@Override
	public String toString() {
		return "Result:" + SUCCESS + " " + TIMING + " msecs ";
	}
	
}