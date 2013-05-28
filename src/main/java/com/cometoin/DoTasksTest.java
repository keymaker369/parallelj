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

public final class DoTasksTest {

	// PRIVATE
	private static List<Integer> waitTimes = null;

	public static final void main(String... aArgs) {

		DoTasksTest tasker = new DoTasksTest();
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

	

	private static void log(Object aMsg) {
		System.out.println(String.valueOf(aMsg));
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