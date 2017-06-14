package com.example.sort;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;

import com.example.util.Measured;

/** Featuras all typical sorting algorithms */

public class SortSample extends Measured {

	/**
	 * Heap sort - heapify the array and 'delete' iteratively the last element
	 */
	public static void heapsort(int[] arr) {
		for (int i = arr.length / 2 - 1; i >= 0; i--)
			heapify(arr, arr.length, i);

		for (int i = arr.length - 1; i >= 0; i--) {
			int temp = arr[0];
			arr[0] = arr[i];
			arr[i] = temp;
			heapify(arr, i, 0);
		}
	}

	/** Heapify algorithm - ensures a subtree is a heap with i as the root */
	private static void heapify(int[] arr, int len, int i) {
		int l = 2 * i + 1; // left = 2*i + 1
		int r = 2 * i + 2; // right = 2*i + 2

		int root = i;
		if (l < len && arr[l] > arr[root])
			root = l;
		if (r < len && arr[r] > arr[root])
			root = r;

		if (root == i)
			return;

		int swap = arr[i];
		arr[i] = arr[root];
		arr[root] = swap;
		heapify(arr, len, root);
	}

	/**
	 * This is a sort algorithm using Java's heap (PriorityQueue). Puts all
	 * elements in the heap and removes them one by one. Not really efficient especially because
	 * the PriorityQueue cannot be heapified in linear time.
	 * 
	 * @param arr
	 */
	public static void sortWithHeap(int[] arr) {
		Queue<Integer> queue = new PriorityQueue<Integer>();
		for (int i : arr)
			queue.offer(i);
		for (int i = 0; i < arr.length; i++)
			arr[i] = queue.poll();
	}

	/** Quick sort */
	public static void quicksort(int[] arr) {
		qsort(arr, 0, arr.length - 1);
	}

	/** Implement a quick sort by partitioning the array around a randomly picked pivot */
	private static void qsort(int[] arr, int low, int high) {
		int pivot = arr[low + (high - low) / 2];
		int i = low, j = high;
		while (i <= j) {
			while (arr[i] < pivot)
				i++;
			while (arr[j] > pivot)
				j--;

			if (i <= j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				i++;
				j--;
			}
		}

		if (j > low)
			qsort(arr, low, j);
		if (i < high)
			qsort(arr, i, high);
	}

	/** Wrapper for merge sort */
	public static void mergesort(int[] arr) {
		msort(arr, 0, arr.length - 1);
	}

	/** Merge sort algorithm implementation - sorts and merges subarrays
	 * every time cutting the problem into half
	 * 
	 * @param arr
	 * @param low
	 * @param hi
	 */
	private static void msort(int[] arr, int low, int hi) {
		if (hi <= low)
			return; // done
		int mid = low + (hi - low) / 2;
		msort(arr, low, mid);
		msort(arr, mid + 1, hi);
		merge(arr, low, mid, hi);
	}

	/** Helper method for the merge sort which merges two arrays into one
	 * using a helper array
	 * 
	 */
	private static void merge(int[] orig, int low, int mid, int hi) {
		int[] result = new int[hi - low + 1];

		int idx = 0;
		int left = low;
		int right = mid + 1;
		while (left <= mid && right <= hi) {
			if (orig[left] <= orig[right]) {
				result[idx++] = orig[left++];
			} else {
				result[idx++] = orig[right++];
			}
		}
		while (left <= mid)
			result[idx++] = orig[left++];

		System.arraycopy(result, 0, orig, low, result.length);
	}
	

	/** Utility method to measure the timing of a consumer algorithm
	 * 
	 * @param what
	 * @param lambda
	 * @param arg
	 */
	public static <T> void measure(String what, Consumer<T> lambda, T arg) {
		long start = System.nanoTime();
		lambda.accept(arg);
		long runtime = (System.nanoTime() - start) / 1000;
		System.out.println("Computed " + what + " in " + runtime + " ms");
	}

	public static void main(String[] args) {
		int[] arr = new int[500];
		Random r = new Random();
		for (int i = 0; i < arr.length; i++)
			arr[i] = r.nextInt(10000);

		System.out.println(Arrays.toString(arr));

		int[] arr2 = arr.clone();
		measure("heap", SortSample::heapsort, arr2);
		System.out.println(Arrays.toString(arr2));

		int[] arr3 = arr.clone();
		measure("with a heap", SortSample::sortWithHeap, arr3);
		System.out.println(Arrays.toString(arr3));

		int[] arr4 = arr.clone();
		measure("quick", SortSample::quicksort, arr4);
		System.out.println(Arrays.toString(arr4));

		int[] arr5 = arr.clone();
		measure("merge", SortSample::mergesort, arr5);
		System.out.println(Arrays.toString(arr5));

	}
}
