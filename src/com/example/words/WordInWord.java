package com.example.words;

import java.util.Arrays;

/**
 * Find a number of times a word occurs in another word. Sequence of the
 * characters has to match but there can be a number of characters in the
 * second word between characters of the first word. 
 * 
 * e.g. 'at' in 'attract' is 4: 'ATtract' 'AtTract' 'AttracT' 'attrAcT'
 *
 */
public class WordInWord {

	/**
	 * Naive recursive algorithm for searching all occurrences of a word in a
	 * word. It tries to match the first character and then does a recursive
	 * call to match the rest.
	 * 
	 * 
	 * Produces an exponential runtime for the worst case which is if the words
	 * are the same and consist of a single character, eg. "aaaa" in "aaaa" In
	 * this case recursion does all n-1 calls so runtime is exponential T(n) = c
	 * + T(n-1) + T(n-2) + T(n-3) ... = 2c + 2T(n-2) + 2T(n-3) + ... = O(c* 2^n)
	 * 
	 */
	public static int contains(String what, String where) {
		System.out.println(what + " in " + where);

		if (what == null || where == null)
			throw new IllegalArgumentException("Arguments must not be null");

		if (what.equals(""))
			return 0;

		int result = 0;
		char a = what.charAt(0);

		for (int i = 0; i < where.length(); i++) {
			char b = where.charAt(i);
			if (a == b) {
				if (what.length() == 1)
					result++;
				else {
					result += contains(what.substring(1), where.substring(i + 1));
				}
			}
		}
		System.out.println(what + " in " + where + " = " + result);
		return result;

	}

	/**
	 * Table based algorithm that builds a table with all prefixes. This
	 * implementation works backwards and matches the last character to which
	 * the postfixes are added.
	 * 
	 */
	public static int containsWithBWTable(String what, String where) {

		if (what == null || where == null)
			throw new IllegalArgumentException("Arguments must not be null");

		if (what.equals("") || where.equals(""))
			return 0;

		// the result table will show how many times we have seen the previous
		// string sequence
		// starting at the index 'i'

		int result[][] = new int[what.length() + 1][where.length() + 1];

		for (int i = 0; i <= where.length(); i++)
			result[what.length()][i] = 1; // init the tables last row with all
											// '1's

		for (int j = what.length() - 1; j >= 0; j--) {
			// Loop the characters backwards
			char a = what.charAt(j);
			int seen = 0; // how many times we have seen 'a' before?

			for (int i = j + where.length() - what.length(); i >= 0; i--) {
				// work backwards on the string but consider length of substring

				if (a == where.charAt(i))
					seen += result[j + 1][i + 1];

				// if we just saw (another) 'a' so all the postfixes that
				// complete it need to be added
				// this is in the result of the last iteration

				result[j][i] = seen;
			}
			System.out.println("  " + a + ": " + Arrays.toString(result[j]));
		}

		return result[0][0];
	}

	/**
	 * A bit simpler table based algorithm that works forward. Iteration j
	 * matches the first j characters of the string. When a match is found
	 * it will be added to the results of the previous iteration.
	 * 
	 * 
	 */
	public static int containsWithFWTable(String what, String where) {

		if (what == null || where == null)
			throw new IllegalArgumentException("Arguments must not be null");

		if (what.equals("") || where.equals(""))
			return 0;

		// the result table will show how many times we have seen the previous
		// string sequence
		// result[j][i] is the number of the first j characters matching at
		// index i

		int result[][] = new int[what.length()][where.length()];

		for (int j = 0; j < what.length(); j++) {
			char a = what.charAt(j);
			int seen = 0; // how many times we have seen 'a' before?

			for (int i = j; i < where.length(); i++) {
				if (a == where.charAt(i)) {
					// saw another 'a' so add the number of prefixes
					// to whatever we saw this far
					if (j > 0)
						seen += result[j - 1][i - 1]; // i > j > 0 !
					else
						seen++; // for the first iteration
				}

				result[j][i] = seen;
			}
			System.out.println("  " + a + ": " + Arrays.toString(result[j]));
		}

		return result[what.length() - 1][where.length() - 1];
	}

	public static void main(String[] args) {
		
		int result;
		
		result = contains("at", "attract");
		System.out.println("Result: "+result);
		result = containsWithFWTable("at", "attract");
		System.out.println("Result: "+result);
		result = containsWithBWTable("at", "attract");
		System.out.println("Result: "+result);
	}

}
