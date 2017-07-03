package com.example.words;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Sample implementation of the infamous word search problem (find out if a
 * string is composed of words given in a dictionary). It's written in all three
 * different approaches: naive recursion, a memoized recursion and a table
 * 
 * The algorithms keep track of how many times a method was called so you can
 * see the breakdown of recursion in all cases.
 * 
 * @author jozseforosz
 *
 */
public class WordSearcher {

	private Map<String, Integer> runtime;

	Set<String> dict;

	private Map<String, Boolean> memo = new HashMap<String, Boolean>();

	public WordSearcher(Set<String> dict) {
		this.dict = dict;
		this.runtime = new HashMap<String, Integer>();
	}

	/**
	 * Naive recursive isMadeOf algorithm which is quite effective, however can
	 * have an exponential runtime for the worst case where the dictionary is
	 * all prefixes of the same word and this is the same string we are looking
	 * for
	 * 
	 * For example: dictionary = { a, aa, aaa, aaaa, aaaaa...}
	 * 
	 * For this case the runtime is T(n) = 1 + T(n-1) + T(n-2) + T(n-3)... //
	 * expand T(n-1) = 1 + 1 + 2T(n-2) + 2T(n-3) + 2T(n-4)... ==> A metric
	 * series adding up to (n + 2^n)
	 * 
	 */
	public boolean isMadeOf(String s) {
		assert s != null;

		markRun(s);
		if ("".equals(s))
			return true;

		boolean result = false;
		for (int i = 1; i <= s.length(); i++) {
			String w = s.substring(0, i);
			if (dict.contains(w)) {
				result = result || isMadeOf(s.substring(i)); // Note that ||
																// short
																// circuits so
																// can reduce
																// recursion
			}
		}

		return result;
	}

	/**
	 * Memoized version of the naive recursive algorithm - is exactly the same
	 * as the naive recursive algorithm except for the use of the memo
	 *
	 * Runtime is O(n) however number of recursions can still be 2^n
	 * 
	 */
	public boolean memoMadeOf(String s) {
		assert s != null;

		Boolean mmo = memo.get(s);
		if (mmo != null) {
			return mmo.booleanValue();
		}

		markRun(s);
		if ("".equals(s))
			return true;

		boolean result = false;
		for (int i = 1; i <= s.length(); i++) {
			String w = s.substring(0, i);
			if (dict.contains(w)) {
				result = result || memoMadeOf(s.substring(i));
			}
		}

		memo.put(s, result);
		return result;
	}

	/**
	 * Tabular version of the algorithm which uses a table of boolean x n where
	 * res[i] indicates if the string starting at index i can be split into
	 * words. Works backwards from the end of the string.
	 * 
	 */
	public boolean tableMadeOf(String s) {
		assert s != null;

		boolean[] res = new boolean[s.length() + 1];
		res[s.length()] = true; // We'll add a 'true' so we can be lazy and not
								// bother with an empty ending

		for (int i = s.length() - 1; i >= 0; i--) {
			// Compute the i-th value: can substring(i -> s.length) be split

			for (int j = 1; j <= s.length() - i; j++) {
				// It can be split if substring(i->j) is a dictionary word
				// And the rest of the string (j->s.length) can be split
				// Because we work backwards on i we know that already so we can
				// use res[i+j] from the table

				System.out.print("i: " + i + " j: " + j);

				String test = s.substring(i, i + j);
				System.out.println(" test: " + test + " (" + dict.contains(test) + ") res[i+j]: " + res[i + j]);

				res[i] = res[i] || dict.contains(test) && res[i + j];
			}
			System.out.println(i + " is " + res[i]);
		}

		return res[0];
	}

	/**
	 * This is an enhancement of the table based algorithm that also keeps track
	 * of the actual strings and in the end produces all words that could make
	 * up the string (if any).
	 * 
	 * The only difference is that instead of a boolean array we now use an
	 * array of arrays res[i] is an array of strings with all possible splits
	 * for the substring at index [i]. If substring(i->s.lenght) cannot be split
	 * res[i] is an empty array
	 * 
	 * @param s
	 * @return
	 */
	public String[] splitAll(String s) {
		assert s != null;

		String[][] res = new String[s.length()][];

		for (int i = s.length() - 1; i >= 0; i--) {
			List<String> rs = new ArrayList<String>();

			for (int j = 1; j <= s.length() - i; j++) {
				System.out.print("i: " + i + " j: " + j);

				String test = s.substring(i, i + j);
				System.out.println(" test: " + test + " (" + dict.contains(test) + ")");

				if (dict.contains(test)) {
					if (i + j < s.length()) {
						// We combine in res[i+j]. Note that if res[i+j] cannot
						// be split
						// then this iteration doesn't do anything so we add
						// nothing at [i] either
						for (String p : res[i + j]) {
							rs.add(test + " " + p);
						}
					} else {
						rs.add(test);
					}
				}
			}

			System.out.println("" + i + " -> " + rs.toString());
			res[i] = rs.toArray(new String[0]);
		}

		return res[0];
	}

	/** Helper method to mark what substring was looked at and how many times */
	private void markRun(String s) {
		int rt = runtime.getOrDefault(s, new Integer(0)).intValue();
		runtime.put(s, rt + 1);
	}
	
	/** Helper method to mark what substring was looked at and how many times. Dumps and resets. */
	public void dumpRuntime() {
		runtime.forEach((String k, Integer v) -> {
			System.out.println("run " + k + " x " + v);
		});

		runtime = new HashMap<String, Integer>();
	}

	public static void main(String[] args) {
		HashSet<String> dict = new HashSet<String>();

		String[] words = { "a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa" };
		dict.addAll(Arrays.asList(words));

		boolean result;

		WordSearcher ws = new WordSearcher(dict);
		result = ws.isMadeOf("aaaaab");
		System.out.println(result);
		ws.dumpRuntime();

		result = ws.memoMadeOf("aaaaab");
		System.out.println(result);
		ws.dumpRuntime();

		result = ws.tableMadeOf("aaaaab");
		System.out.println(result);

		String[] splits = ws.splitAll("aaaaaaaaaaa");
		System.out.println(Arrays.toString(splits));

	}

}
