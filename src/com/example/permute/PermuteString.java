package com.example.permute;

import java.util.Arrays;

/** Sample class to produce the permuations of characters/strings given an input string in the form of 'aaa*b*d' 
 * where * is a placeholder and each of these * would be replaced with all
 * permutations of the set of chars or strings
 * 
 * @author jozseforosz
 *
 */
public class PermuteString {

	private static final String[] values = { "1", "2" };

	/** Permutation - obviously has v^n runtime ( v=number of characters, n=number of stars) 
	 * but linear recursion */	
	public static String[] permute(String pattern) {
		System.out.println("Permute " + pattern);
		
		assert pattern != null;
		
		int idx = pattern.indexOf("*");
		if (idx < 0) { 
			// No match - nothing to permute
			// Works for empty string too
			return new String[] { pattern };
		}

		String pre = pattern.substring(0, idx);
		String[] others = permute(pattern.substring(idx + 1));

		String[] result = new String[values.length * others.length];
		int i = 0;
		for (String value : values) {
			for (String rest : others) {
				result[i++] = pre + value + rest;
			}
		}

		return result;
	}

	public static void main(String[] args) {
		String[] alma1 = permute("aa*bb**");
		System.out.println(Arrays.toString(alma1));
		alma1 = permute("a*a**a");
		System.out.println(Arrays.toString(alma1));
		alma1 = permute("no_star");
		System.out.println(Arrays.toString(alma1));
		alma1 = permute("***");
		System.out.println(Arrays.toString(alma1));
	}
}
