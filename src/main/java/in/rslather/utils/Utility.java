package in.rslather.utils;

import java.lang.reflect.Array;

public class Utility {
	private static final ThreadLocal<Object> MONITOR = ThreadLocal
			.withInitial(Object::new);

	public static long measureElapsedTime(final Runnable runnable) {
		final long startTime = System.currentTimeMillis();
		runnable.run();
		return System.currentTimeMillis() - startTime;
	}

	public static void printElapsedTime(final Runnable runnable) {
		System.out.println("Elapsed time is " + measureElapsedTime(runnable));
	}

	public static void waitingSleep(final long timeInMilliseconds)
			throws InterruptedException {
		final long startTime = System.currentTimeMillis();
		final Object monitor = Utility.MONITOR.get();
		synchronized (monitor) {
			long loopTime = System.currentTimeMillis() - startTime;
			while (loopTime < timeInMilliseconds) {
				monitor.wait(timeInMilliseconds - loopTime);
				loopTime = System.currentTimeMillis() - startTime;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] buildAsArray(final Class<T> clazz, final int capacity) {
		return (T[]) Array.newInstance(clazz, capacity);
	}

	private Utility() {
		// private
	}
}
