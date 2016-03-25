package com.java8.exercises;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Exercises from Chapter 1.
 * Lambda Expressions
 * 
 * @author sky.lin
 *
 */
public class Chapter1 {
	
	class LengthComparator implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			return Integer.compare(o1.length(), o2.length());
		}
	}
	
	interface RunnableEx {
		public void run() throws Exception;
	}
	
	// Problem 6
	public static Runnable uncheck(RunnableEx runner) {
		return () -> {
			try {
				runner.run();
			} catch (Exception e) {
				// ignore all exceptions
			}
		};
	}
	
	public static Runnable andThen(Runnable x, Runnable y) {
		return ()-> {x.run(); y.run();};
	}
	
	public static void main(String[] args) {
		
		ArrayList<String> strings = new ArrayList<String>();
		strings.add("test");
		strings.add("java8");
		strings.add("omglambdas");
		//Arrays.sort((String[])strings.toArray(), new LengthComparator());
		
		Comparator<String> comp = (String first, String second) -> {
			return Integer.compare(first.length(), second.length());
		};
		// Problem 1
		Arrays.sort(args, comp);
		
		repeatMessage("Hello", 10);
		
		// Problem 2
		Function<File, File[]> getSubDirs = (File dir) -> {
			return dir.listFiles(java.io.File::isDirectory);
		};
		
		// Problem 3
		BiFunction<File, String, File[]> getFilesWithExt = (File dir, String ext) -> {
			return dir.listFiles((File file) -> file.toString().endsWith(ext));
		};
		
		// Problem 4
		Comparator<File> sortFiles = (first, second) -> {
			if (first.isDirectory() && !second.isDirectory()) return -1;
			if (second.isDirectory() && !first.isDirectory()) return 1;
			return first.getName().compareTo(second.getName());
		};
		
		// Problem 6
		new Thread(uncheck(
				() -> {System.out.println("Zzz"); Thread.sleep(1000); })).start();
		
		// Problem 7
		andThen(()->System.out.println("Go"), ()->System.out.println("Stop")).run();
		
		// Problem 8
		String [] names = {"Peter", "Paul", "Mary"};
		List<Runnable> runners = new ArrayList<>();
		for (String name: names)
			runners.add(()->System.out.println(name));
		for(Runnable runner: runners)
			runner.run();
		
	}
	
	// Problem 9
	public abstract class Collection2<T> implements Collection<T> {
		public void forEachIf(Consumer<T> action, Predicate<T> filter) {
			Iterator<T> iter = iterator();
			while (iter.hasNext()) {
				T it = iter.next();
				if (filter.test(it))
					action.accept(it);
			}
		}
	}
	
	/*
	 * lambdas need 3 ingredients:
	 * 1. block of code
	 * 2. parameters (or no parameters)
	 * 3. values for free variables (variables outside of its scope)
	 */
	public static void repeatMessage(String text, int count) {
		Runnable r = () -> {
			for (int i = 0; i < count; i++) {
				System.out.println(text);
				Thread.yield();
			}
		};
		new Thread(r).start();
	}
}
