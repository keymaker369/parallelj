package com.cometoin;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TasksExecutor {

	private List<Task> tasks;
	private int numThreads;
	private ExecutionOption executionOption;

	public TasksExecutor(List<Task> tasks, int numThreads, ExecutionOption executionOption) {
		this.tasks = tasks;
		this.numThreads = numThreads;
		this.executionOption = executionOption;
		
	}

	public TasksExecutor(List<Task> tasks, ExecutionOption executionOption) {
		this.tasks = tasks;
		this.executionOption = executionOption;
	}
	
	public TasksExecutorResult run() {
		try {

			switch (executionOption) {
			case PARALLEL:
				return runParallel();

			case SEQUENTIALL:
				return runSequeniall();

			default:
				throw new RuntimeException("ExecutionOption should be PARALLEL or SEQUENTIALL");
			}
			
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException ex) {
			log("Problem executing worker: " + ex.getCause());
		} catch (Exception ex) {
			log("Problem: " + ex.getCause());
		} 
		log("Done.");
		return null;
	}

	private TasksExecutorResult runParallel() throws InterruptedException, ExecutionException {

		long start = System.currentTimeMillis();

		// threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<Future<TaskResult>> results = executor.invokeAll(tasks);
		for (Future<TaskResult> result : results) {
			TaskResult taskResult = result.get();
			log(taskResult);
		}

		long end = System.currentTimeMillis();

		executor.shutdown(); // always reclaim resources

		long duration = (end - start) / 1000;
		
		System.out.println("Duration in seconds: " + duration);
		
		return new TasksExecutorResult(true, duration);
		
		
	}

	private TasksExecutorResult runSequeniall() throws Exception {
		long start = System.currentTimeMillis();

		for (Task task : tasks) {
			TaskResult taskResult = task.call();
			log(taskResult);
		}

		long end = System.currentTimeMillis();

		long duration = (end - start) / 1000;
		
		System.out.println("Duration in seconds: " + duration);
		
		return new TasksExecutorResult(true, duration);

	}

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}
}
