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
				runParallel();
				break;

			case SEQUENTIALL:
				runSequeniall();
				break;

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

	private void runParallel() throws InterruptedException, ExecutionException {

		long start = System.currentTimeMillis();

		// threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<Future<TaskResult>> results = executor.invokeAll(tasks);
		for (Future<TaskResult> result : results) {
			TaskResult taskResult = result.get();
			log(taskResult);
		}

		long end = System.currentTimeMillis();

		System.out.println("Duration in seconds: " + ((end - start) / 1000));

		executor.shutdown(); // always reclaim resources

	}

	private void runSequeniall() throws Exception {
		long start = System.currentTimeMillis();

		for (Task task : tasks) {
			TaskResult taskResult = task.call();
			log(taskResult);
		}

		long end = System.currentTimeMillis();

		System.out.println("Duration in seconds: " + ((end - start) / 1000));

	}

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}
}
