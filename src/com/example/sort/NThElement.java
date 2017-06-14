package com.example.sort;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.example.util.Measured;

/**
 * Creative ways to find the n'th element of an array of numbers. A generic case
 * for finding a median or other values.
 * 
 * @author jozseforosz
 *
 */
public class NThElement extends Measured {

	static void swap(int[] numbers, int left, int right) {
		int temp = numbers[left];
		numbers[left] = numbers[right];
		numbers[right] = temp;
	}

	/**
	 * Functional interface so that partitioning is swappable so it's possible
	 * to try multiple algorithms.
	 * 
	 */
	@FunctionalInterface
	static interface PartitionFunction {

		int partition(int[] numbers, int low, int hi, int pivotIndex);

	}

	/**
	 * Partitions the array between low and hi indexes using the pivot at the
	 * index pivotIndex. This algorithm is the trivial algorithm which works
	 * well with duplicate values of the pivot. Runtime is O(n) but in fact it
	 * is always 'n' since the for loop runs through the entire range and also
	 * makes a lot of swaps.
	 */
	public static int partitionTrivial(int[] numbers, int low, int hi, int pivotIndex) {
		int pivot = numbers[pivotIndex];

		// Swap 'hi' into the pivot's place so we can go until hi - 1
		swap(numbers, hi, pivotIndex);

		// Partition array by member
		int q = low;
		for (int j = low; j < hi; j++) {
			if (numbers[j] <= pivot) {
				// swap a[j] with a[q]
				swap(numbers, j, q);
				q++;
			}
		}

		// Should terminate at some q value which is the place for the pivot
		swap(numbers, hi, q);
		return q;
	}

	/**
	 * A faster algorithm to partition the array between low and hi indexes
	 * using the pivot at the index pivotIndex. This algorithm is an optimized
	 * algorithm which loops from left & right but it's been modified to work
	 * well with duplicate values of the pivot.
	 * 
	 * It has also been modified to ensure the pivot is placed into its
	 * position. It does not put all instances of the pivot into position but
	 * one instance of it will be in place (others to the left).
	 * 
	 * Runtime is O(n) and this algorithm minimizes the number of swaps.
	 */
	public static int partitionFast(int[] numbers, int low, int hi, int pivotIndex) {
		int pivot = numbers[pivotIndex];

		// Swap the pivot into 'low' so we get it out of the way
		swap(numbers, low, pivotIndex);

		// Partition array by member
		int left = low;
		int right = hi;

		while (left < right) {
			while (left <= hi && numbers[left] <= pivot)
				left++;
			while (right >= low && numbers[right] > pivot)
				right--;

			if (left < right) {
				swap(numbers, left, right);
			}
		}

		// Should terminate at some 'right' value which is the place for the
		// pivot
		swap(numbers, low, right);
		return right;
	}

	/**
	 * A tweaked algorithm to partition the array between low and hi indexes
	 * using the pivot but by knowing the value of the pivot and not it's index.
	 * This makes it harder since we cannot simply swap it out of the way.
	 * 
	 * It has also been modified to ensure the pivot is placed into its
	 * position. It does not put all instances of the pivot into position but
	 * one instance of it will be in place (others to the left).
	 * 
	 * Runtime is O(n) and this algorithm minimizes the number of swaps.
	 */
	public static int partitionWithValue(int[] numbers, int low, int hi, int pivot) {
		int left = low;
		int right = hi;
		int pivotat = -1;

		while (left < right) {
			while (left <= hi && numbers[left] <= pivot) {
				if (numbers[left] == pivot && pivotat == -1)
					pivotat = left;
				left++;
			}
			while (right >= low && numbers[right] > pivot)
				right--;

			if (left < right) {
				swap(numbers, left, right);
			}
		}

		if (pivotat > -1)
			swap(numbers, pivotat, right);

		return right;
	}

	private static final Random random = new Random();

	/**
	 * Linear algorithm to find the n'th value of the array. Is based on picking
	 * a pivot value and partitioning the array based on the pivot. The
	 * partitioning should return the position of the pivot giving us it's rank.
	 * If the number we're looking for is smaller than the pivot we only need to
	 * look in the left half, if greater in the right half .. so we divided the
	 * work & can make a recursive call.
	 * 
	 * Runtime is linear if the pivot is picked 'correctly' that is it splits
	 * the interval into roughly equal sizes. In this case: T(n) = n (partition)
	 * + T(n/2) <= 2n (harmonic number which is smaller than 2)
	 *
	 * In the worst case it's quadratic T(n) = n + T(n-1) = n + n-1 + T(n-2) <=
	 * O(n^2).
	 * 
	 * @param pf
	 *            the partitioning function to use
	 */
	public static int nthof(int[] numbers, int position, int low, int hi, PartitionFunction pf) {
		// System.out.println("nthof "+position+" in "+low+" - "+hi);

		assert hi >= low;
		assert low >= 0 && low < numbers.length;
		assert hi < numbers.length;
		assert position - 1 <= hi && position - 1 >= low;

		// Pick a random element
		if (hi == low)
			return numbers[low];

		int pidx = low + random.nextInt(hi - low);
		int pivot = numbers[pidx];

		int q = pf.partition(numbers, low, hi, pidx);

		if (q == position - 1)
			return pivot;
		else if (position < q + 1)
			return nthof(numbers, position, low, q - 1, pf);
		else
			return nthof(numbers, position, q + 1, hi, pf);
	}

	/** Helper method to establish the median of 5 numbers */
	public static void median5(int[] a, int i) {
		if (a[i] < a[i + 1])
			swap(a, i, i + 1);
		if (a[i + 2] < a[i + 3])
			swap(a, i + 2, i + 3);

		if (a[i] < a[i + 2]) {
			swap(a, i, i + 2);
			swap(a, i + 1, i + 3);
		}

		if (a[i + 1] < a[i + 4])
			swap(a, i + 1, i + 4);

		if (a[i + 1] > a[i + 2]) {
			if (a[i + 2] < a[i + 4])
				swap(a, i + 2, i + 4);
		} else {
			if (a[i + 1] > a[i + 3])
				swap(a, i + 1, i + 2);
			else
				swap(a, i + 3, i + 2);
		}
	}

	/** Helper method to establish the median of 4 numbers */
	public static void median4(int[] a, int i) {
		if (a[i] < a[i + 1])
			swap(a, i, i + 1);
		if (a[i + 2] < a[i + 3])
			swap(a, i + 2, i + 3);

		if (a[i] < a[i + 2]) {
			swap(a, i, i + 2);
			swap(a, i + 1, i + 3);
		}

		if (a[i + 1] < a[i + 2]) {
			if (a[i + 1] > a[i + 3])
				swap(a, i + 1, i + 2);
			else
				swap(a, i + 3, i + 2);
		}
	}

	/** Helper method to establish the median of 3 numbers */
	public static void median3(int[] a, int i) {
		if (a[i] < a[i + 2])
			swap(a, i, i + 2);

		if (a[i] > a[i + 1])
			swap(a, i, i + 1);

		if (a[i + 1] > a[i + 2])
			swap(a, i + 1, i + 2);
	}

	/**
	 * A mathematically correct algorithm to find the n'th value of the array.
	 * Is based on picking a pivot value scientifically and partitioning the
	 * array based on the pivot.
	 * 
	 * Picking of the pivot is sophisticated but the pivot is then guaranteed to
	 * be at least n/4 distance from both the minimum and the maximum so even in
	 * the worst case we need to look at 3/4's of elements.
	 * 
	 * Runtime is guaranteed to be linear (see books)
	 * 
	 * T(n) = n (partition) + 6n + T(n/5) (pick pivot) + T(3/4n) <= 44n
	 * 
	 * Picking the pivot is based on dividing the list into groups of 5,
	 * computing the median of these 5's using a quick compare with 6 steps
	 * (@see median5()) then establishing the median of these medians by a
	 * recursive call to the same algorithm. The median of the medians is the
	 * pivot which is indeed greater and smaller than 1/4 of the elements.
	 * 
	 * 
	 * In practice this algorithm is not that fast and is only faster with a LOT
	 * of elements, never faster than picking the pivot randomly.
	 */
	public static int supernthof(int[] numbers, int position, int low, int hi) {
		// System.out.println("Supernthof "+position+" in "+low+" - "+hi);

		assert hi >= low;
		assert low >= 0 && low < numbers.length;
		assert hi < numbers.length;
		assert position - 1 <= hi && position - 1 >= low;

		if (hi == low)
			return numbers[low];

		// Now we pick a 'member' to be used as a pivot
		// A member is larger than 1/4th of the elements and smaller than 1/4th
		// of the elements
		int pivot;

		int j = low;
		for (int i = low; i + 4 < hi; i += 5) {
			// In groups of 5
			median5(numbers, i); // 'half-sort' the 5 numbers
			// i+2 has the median of the 5 numbers
			swap(numbers, i + 2, j++);
			// swap median into the j'th position (ie the beginning of list)
		}

		int groups = j - low;
		if (groups > 5) {
			pivot = supernthof(numbers, low + groups / 2, low, j - 1);
		} else if (groups == 5) {
			median5(numbers, low);
			pivot = numbers[low + 2];
		} else if (groups == 4) {
			median4(numbers, low);
			pivot = numbers[low + 2];
		} else if (groups == 3) {
			median3(numbers, low);
			pivot = numbers[low + 1];
		} else if (groups == 1 || groups == 2) {
			pivot = numbers[low];
		} else {
			pivot = numbers[(hi - low) / 2 + low];
		}

		// System.out.println("Pivot "+pivot+" for "+low+" - "+hi);

		int q = partitionWithValue(numbers, low, hi, pivot);

		if (q == position - 1)
			return pivot;
		else if (position < q + 1)
			return supernthof(numbers, position, low, q - 1);
		else
			return supernthof(numbers, position, q + 1, hi);
	}

	public static void main(String[] args) {

		// Test for median
		int[] mt1 = { 4, 5, 1, 8 };
		median4(mt1, 0);
		System.out.println(Arrays.toString(mt1));

		// Tests for partition
		int[] test = { 5, 7, 1 };
		int result = partitionWithValue(test, 0, test.length - 1, 5);
		System.out.println(Arrays.toString(test));
		System.out.println(result + " = " + test[result]);
		result = partitionTrivial(test, 0, test.length - 1, result);
		System.out.println(Arrays.toString(test));
		System.out.println(result + " = " + test[result]);

		
		// Test for n'th element
		final int[] arr = new int[199000000];
		Random r = new Random();
		for (int i = 0; i < arr.length; i++)
			arr[i] = r.nextInt(500000);

		final int LOOK_FOR = r.nextInt(arr.length);

		final int[] orig = arr.clone(); // Backup array for resetting

		measure("partition trivial", () -> nthof(arr, LOOK_FOR, 0, arr.length - 1, NThElement::partitionTrivial));

		System.arraycopy(orig, 0, arr, 0, arr.length);
		measure("partition fast", () -> nthof(arr, LOOK_FOR, 0, arr.length - 1, NThElement::partitionFast));

		System.arraycopy(orig, 0, arr, 0, arr.length);
		measure("supernthof", () -> supernthof(arr, LOOK_FOR, 0, arr.length - 1));

		System.arraycopy(orig, 0, arr, 0, arr.length);
		measure("sort", () -> { Arrays.sort(arr); return arr[LOOK_FOR - 1]; } );

	}

}
