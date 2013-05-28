package com.cometoin;

import java.util.concurrent.Callable;

public abstract class Task implements Callable<TaskResult> {

	public abstract TaskResult call() throws Exception;
	
}