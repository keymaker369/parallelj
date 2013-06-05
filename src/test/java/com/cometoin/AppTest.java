package com.cometoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		List<Integer> waitTimes = null;
		waitTimes = this.genrateArrayListOfINTs(7);
		List<Task> tasks = new ArrayList<>(waitTimes.size());
		for (int i = 0; i < waitTimes.size(); i++) {
			tasks.add(i, new SampleTask(waitTimes.get(i)));
		}

		log("Array wait time SUM: " + this.calculateWaitTimeSum(waitTimes));

		TasksExecutor tasksParallelExecutor = new TasksExecutor(tasks, 2, ExecutionOption.PARALLEL);
		TasksExecutorResult parallelResult = tasksParallelExecutor.run();
		TasksExecutor tasksSequentialExecutor = new TasksExecutor(tasks, 3, ExecutionOption.SEQUENTIALL);
		TasksExecutorResult sequentialResult = tasksSequentialExecutor.run();

		assertTrue(parallelResult.secconds < sequentialResult.secconds);
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
