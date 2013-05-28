package com.cometoin;

import java.net.MalformedURLException;
import java.util.ArrayList;
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

import com.cometoin.sample.SampleTask;

public final class DoTasks {

	// PRIVATE
	private static List<Integer> waitTimes = null;

	public static final void main(String... aArgs) {

		DoTasks tasker = new DoTasks();
		waitTimes = tasker.genrateArrayListOfINTs(48);
		List<Task> tasks = new ArrayList<>(waitTimes.size());
		for (int i = 0; i < waitTimes.size(); i++) {
			tasks.add(i, new SampleTask(waitTimes.get(i)));
		}
		
		log("Array wait time SUM: " + tasker.calculateWaitTimeSum(waitTimes));

		TasksExecutor tasksParallelExecutor = new TasksExecutor(tasks, 8, ExecutionOption.PARALLEL);
		tasksParallelExecutor.run();
		TasksExecutor tasksSequentialExecutor = new TasksExecutor(tasks, 8, ExecutionOption.SEQUENTIALL);
		tasksSequentialExecutor.run();
		
	}

	void runAndReportEachWhenKnown() throws InterruptedException, ExecutionException {

		long start = System.currentTimeMillis();

		int numThreads = waitTimes.size() > 20 ? 20 : waitTimes.size(); // max 7
		// threads
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		CompletionService<TaskResult> compService = new ExecutorCompletionService<TaskResult>(executor);
		for (Integer seconds : waitTimes) {
			Task task = new SampleTask(seconds);
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
			tasks.add(new SampleTask(seconds));
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