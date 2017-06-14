package com.example.permute;

import java.util.Arrays;
import java.util.Random;

public class PermuteNumbers {

	private static Random r = new Random();

	/**
	 * Shuffles an array of numbers with all probabilities being equally
	 * possible
	 * 
	 * @param arr
	 */
	public static void shuffle(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			// Pick element for index i-arr.length to swap into position i
			// We always pick from the numbers we didn't pick
			int idx = i + r.nextInt(arr.length - i);
			int temp = arr[i];
			arr[i] = arr[idx];
			arr[idx] = temp;
		}
	}

	/**
	 * Creates a permutation of 1,2,3,...n range of numbers by the index n.
	 * Looping n from 0 to n! should present all permutations!
	 * 
	 * Returns a new array, doesn't mutate the original array
	 * 
	 * @param n
	 * @return
	 */
	public static int[] permutationAt(long p, int[] what) {
		assert what != null;
		
		int[] nums = what.clone();
		int radix = what.length;

		for (int i = 0; i < radix; i++)
			nums[i] = i + 1; // Array init to 1,2,3

		for (int i = 0; i < radix; i++) {
			int d = radix - i;
			
			int idx = (int)(p % d) + i; 
			// idx will get a value between i and the remaining elements in
			// the subarray i..n

			p = p / d;

			// swap nums[idx] into nums[i]
			int temp = nums[idx];
			nums[idx] = nums[i];
			nums[i] = temp;
		}

		return nums;
	}
	
	/** A smart way to generate lottery numbers based on a SINGLE random number using the permutation
	 * algorithm above. Basically we use the fact that the generator above permutes the first n elements
	 * of the list in all possible combinations first so for n numbers it will put all combinations
	 * for a range between 0..n! / num! and the output will be the first num elements of the array
	 */
	public static int[] makeLotteryNumbers(int max, int num) {
		int[] numbers = new int[max];
		
		// init array of lottery numbers
		for (int i=0; i< max; i++) numbers[i] = i+1;

		// make a random big enough to cover permutations for the 'num' elements
		// that is max!/(max-num!) which we calculate with *=
		long limit = 1;
		for (int i = 1; i <= num; i++) limit *= (max-i+1);
		
		long rand = (long)(r.nextDouble()*limit);
		System.out.println("Random "+rand+" from range "+limit);
		
		// Shuffle based on this random index
		int[] shuffle = permutationAt(rand, numbers);
		
		// Return the subarray of the shuffle 0..num-1
		// The first elements will all be combined!
		
		int[] result = new int[num];
		System.arraycopy(shuffle, 0, result, 0, num);
		return result;
	}

	/** Helper method for factorial */
	public static long factorial(int n) {
		long fact = 1; 
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
	}

	public static void main(String[] args) {
		int howMany = 4;
		
		long fact = factorial(howMany);
		int[] base = new int[howMany];
		for (int i=0; i<howMany; i++) base[i] = i+1;
		
		for (int j=0; j < fact; j++) {
			int[] perm = permutationAt(j, base);
			System.out.println(Arrays.toString(perm));
		}
		
		
		int[] lottery = makeLotteryNumbers(90, 5);
		System.out.println(Arrays.toString(lottery));
		
	}

}
