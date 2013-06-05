package com.cometoin;

import com.cometoin.Task;
import com.cometoin.TaskResult;

public class SampleTask extends Task {

	private final Integer seconds;
	
	public SampleTask(Integer seconds) {
		this.seconds = seconds;
	}

	@Override
	public TaskResult call() throws Exception  {
		return runAndReportStatus(seconds);
	}

	private TaskResult runAndReportStatus(Integer seconds) {
		TaskResult result = new TaskResult();
		long start = System.currentTimeMillis();
		try {
			Thread.sleep(seconds * 1000);
			result.SUCCESS = true;
			long end = System.currentTimeMillis();
			result.TIMING = end - start;
		} catch (InterruptedException ex) {
			// ignore - fails
		}
		return result;
	}	
	
}
