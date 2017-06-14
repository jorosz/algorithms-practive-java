package com.example.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/** Utility class to measure timing of certain functions passed in as a lambda expression
 * 
 * @author jozseforosz
 *
 */
public abstract class Measured {

	public static <R,T> R measure(String what, Function<T,R> lambda, T arg) {
		long start = System.nanoTime();
		R result = lambda.apply(arg);
		long runtime = System.nanoTime() - start;
		System.out.println("Computed "+what+" in " + runtime/1000000 + " ns, result " + result);
		return result;
	}
	
	public static <T> void measure(String what, Consumer<T> lambda, T arg) {
		long start = System.nanoTime();
		lambda.accept(arg);
		long runtime = System.nanoTime() - start;
		System.out.println("Computed "+what+" in " + runtime/1000000 + " ms");
	}
	
	public static <T> T measure(String what, Supplier<T> lambda) {
		long start = System.nanoTime();
		T result = lambda.get();
		long runtime = System.nanoTime() - start;
		System.out.println("Computed "+what+" in " + runtime/1000000 + " ms with result "+result);
		return result;
	}
	
	
}
