package com.cometoin;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class DoTasks {

	// PRIVATE
	private static List<Integer> waitTimes = null;

	public static final void main(String... aArgs) {

		DoTasks tasker = new DoTasks();
		waitTimes = tasker.genrateArrayListOfINTs(48);

		log("Srray wait time SUM: " + tasker.calculateWaitTimeSum(waitTimes));
		
		try {
			log("Parallel, report each as it completes:");
			tasker.runAndReportEachWhenKnown();

			log("Parallel, report all at end:");
			tasker.runAndReportAllAtEnd();

			log("Sequential, report each as it completes:");
			tasker.runAndReportSequentially();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException ex) {
			log("Problem executing worker: " + ex.getCause());
		} catch (MalformedURLException ex) {
			log("Bad URL: " + ex.getCause());
		}
		log("Done.");
	}

	void runAndReportEachWhenKnown() throws InterruptedException, ExecutionException {

		long start = System.currentTimeMillis();

		int numThreads = waitTimes.size() > 20 ? 20 : waitTimes.size(); // max 7
		// threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		CompletionService<TaskResult> compService = new ExecutorCompletionService<TaskResult>(executor);
		for (Integer seconds : waitTimes) {
			Task task = new Task(seconds);
			compService.submit(task);
		}
		for (int idx = 0; idx < waitTimes.size(); ++idx) {
			Future<TaskResult> future = compService.take();
			log(future.get());
		}

		long end = System.currentTimeMillis();

		System.out.println("Duration in seconds: " + ((end - start) / 1000));

		executor.shutdown(); // always reclaim resources
	}

	void runAndReportAllAtEnd() throws InterruptedException, ExecutionException {

		long start = System.currentTimeMillis();

		Collection<Callable<TaskResult>> tasks = new ArrayList<Callable<TaskResult>>();
		for (Integer seconds : waitTimes) {
			tasks.add(new Task(seconds));
		}
		int numThreads = waitTimes.size() > 20 ? 20 : waitTimes.size(); // max 7
		// threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<Future<TaskResult>> results = executor.invokeAll(tasks);
		for (Future<TaskResult> result : results) {
			TaskResult pingResult = result.get();
			log(pingResult);
		}

		long end = System.currentTimeMillis();

		System.out.println("Duration in seconds: " + ((end - start) / 1000));

		executor.shutdown(); // always reclaim resources
	}

	void runAndReportSequentially() throws MalformedURLException {

		long start = System.currentTimeMillis();

		for (Integer seconds : waitTimes) {
			TaskResult taskResult = runAndReportStatus(seconds);
			log(taskResult);
		}

		long end = System.currentTimeMillis();

		System.out.println("Duration in seconds: " + ((end - start) / 1000));
	}

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
	}

	private final class Task implements Callable<TaskResult> {

		private final Integer seconds;

		Task(Integer seconds) {
			this.seconds = seconds;
		}

		public TaskResult call() throws Exception {
			return runAndReportStatus(seconds);
		}
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

	/** Hold all the date related to a ping. */
	private final class TaskResult {
		Boolean SUCCESS;
		Long TIMING;

		@Override
		public String toString() {
			return "Result:" + SUCCESS + " " + TIMING + " msecs ";
		}
	}

	public ArrayList<Integer> genrateArrayListOfINTs(int size) {
		ArrayList<Integer> integers = new ArrayList<Integer>(size);

		Random random = new Random();

		for (int i = 0; i < size - 1; i++) {
			integers.add(i, random.nextInt(15));
		}

		return integers;
	}

	public int calculateWaitTimeSum(List<Integer> waitTimes) {

		int sum = 0;

		for (Integer integer : waitTimes) {
			sum += integer;
		}

		return sum;
	}
}