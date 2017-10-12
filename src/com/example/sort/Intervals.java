package com.example.sort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.DelayQueue;

/** Playing with intervals, detecting overlaps and optimizing overlaps */
public class Intervals {

	/** Helper class to store an interval */
	static class Interval {

		int start;
		int end;

		Interval(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			return " [ " + start + " -> " + end + " ] ";
		}

		public static final Comparator<Interval> compareByStart = (Interval a, Interval b) -> a.start - b.start != 0
				? a.start - b.start : a.end - b.end;

		public static final Comparator<Interval> compareByEnd = (Interval a, Interval b) -> a.end - b.end != 0
				? a.end - b.end : a.start - b.start;
	}

	@SuppressWarnings("unused")
	private static void dump(int[][] arr) {
		for (int[] i : arr)
			System.out.println("" + i[0] + " - " + i[1]);
	}

	@SuppressWarnings("unused")
	private static void dump(List<Interval> arr) {
		System.out.println(arr.toString());
	}

	private static void dump(Interval[] arr) {
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * Find maximal overlapping interval in a sorted array Uses a heap to keep
	 * track of intervals and minimums of start times to know the overlapping
	 * samples. When a conflict is detected the size of the heap is the number
	 * of overlaps.
	 * 
	 * Runtime is O(n * log m) if sorted intervals are given where m is the
	 * largest overlap. Space is O(m) used by the heap.
	 * 
	 * @param intervals
	 * @return
	 */
	public static int maxOverlap(Interval[] intervals) {
		Queue<Integer> ends = new PriorityQueue<Integer>();
		int max_overlap = 0;

		for (Interval interval : intervals) {
			int start = interval.start;
			int end = interval.end;

			while (!ends.isEmpty() && ends.peek().intValue() < start)
				ends.remove();

			int overlaps = ends.size();
			if (overlaps > 0) {
				System.out.println(start + "-" + end + " overlaps with ends " + ends);
				if (overlaps > max_overlap)
					max_overlap = overlaps;
			}

			ends.offer(end);
		}

		return max_overlap;
	}

	/**
	 * Simplified algorithm to determine the existence of an overlap. In this
	 * case we only need to keep track of the last (max) departure time and any
	 * start time before that is a conflict (again assuming the input is
	 * sorted).
	 * 
	 * Linear time, constant space
	 * 
	 */
	public static boolean hasOverlap(Interval[] intervals) {
		int max_end = -1;

		for (Interval interval : intervals) {
			int start = interval.start;
			int end = interval.end;

			if (start > max_end) {
				// No overlap
				max_end = end;
			} else {
				System.out.println(start + "-" + end + " overlaps with end " + max_end);
				return true;
			}
		}

		return false;
	}

	/** Algorithm which merges overlapping intervals into one. Assuming the intervals are sorted. 
	 * The result is on a 'queue' of depth 1 so it can be extended if required.
	 * 
	 * */
	public static Interval[] mergeAllOverlaps(Interval[] intervals) {

		List<Interval> result = new ArrayList<Interval>();
		Interval last = null;

		for (Interval i : intervals) {
			System.out.println(""+i);
			if (last == null) {
				// We don't have anything so the current entry goes onto the queue.
				last = i;
				System.out.println(last + "to queue");
			} else if (last.end < i.start) {
				System.out.println("" + last + " is OK"); // release previous
				result.add(last);
				last = i; // current goes on queue
			} else {
				int min_start = i.start < last.start ? i.start : last.start;
				// Note: min_start SHOULD be last.start since the array is sorted
				int max_end = last.end > i.end ? last.end : i.end;
				Interval newlast = new Interval(min_start, max_end); 
				
				System.out.println(i + " overlaps with " + last + " merged as" + newlast);
				last = newlast;
			}
		}
		if (last != null)
			result.add(last);

		return result.toArray(new Interval[0]);
	}
	
	/** Algorithm which merges overlapping intervals into one. Assuming the intervals are sorted. 
	 * The result is on a 'queue' of depth 1 so it can be extended if required.
	 * 
	 * */
	public static Interval[] mergeAllOverlapsWithStack(Interval[] intervals) {

		Deque<Interval> result = new ArrayDeque<Interval>();

		for (Interval i : intervals) {
			System.out.println(""+i);
			Interval last = result.peekLast();
			if (last == null || last.end < i.start) {
				result.addLast(i);
			} else {
				// i.start <= last.end -- merge!
				int max_end = last.end > i.end ? last.end : i.end;
				last.end = max_end;
				System.out.println(i + " overlaps with " + last + " merged as" + last);
			}
		}
		
		return result.toArray(new Interval[0]);
	}
	
	
	/** Merges an array of intervals when the array of intervals in unsorted. It does not sort the array but
	 * uses two heaps to keep track of start & departures to generate new intervals.
	 * 
	 */
	public static Interval[] mergeUnsorted(Interval[] intervals) {
		List<Interval> result = new ArrayList<Interval>();
		
		// Build two heaps with the starts and departures
		Queue<Integer> ins = new PriorityQueue<Integer>();
		Queue<Integer> outs = new PriorityQueue<Integer>();
		for (Interval i: intervals) {
			ins.offer(i.start);
			outs.offer(i.end);
		}
		
		int start, end;
		
		while (!ins.isEmpty() && !outs.isEmpty()) {
			// Take the first value from the heap. This is the earliest start
			start = ins.remove();
			end = outs.remove(); // take first exit
			
			while (ins.peek() != null && ins.peek() <= end) {
				end = outs.remove();
				ins.remove();
			}
			
			result.add(new Interval(start,end));

		}
		
		return result.toArray(new Interval[0]);
		
	}
	
	/** Finds the maximum number of overlapping intervals when the list of intervals is unsorted
	 * without actually sorting the array. Uses two heaps to find start times and departures.
	 * 
	 * By peeking at the minimum of both heaps and comparing them it's easy to determine if we have a new
	 * overlap. We have new overlap if start < end and we have one less overlap when end < start. The smaller
	 * value is then removed.
	 * 
	 * Runtime is still n*log(n) as if the array was sorted, in fact it's 4*n*log(n) so possibly slower than sorting.
	 */
	public static int maxOverlapUnsorted(Interval[] intervals) {
		int max_over = -1;

		// Build two heaps with the starts and departures
		Queue<Integer> ins = new PriorityQueue<Integer>();
		Queue<Integer> outs = new PriorityQueue<Integer>();
		for (Interval i: intervals) {
			// PriorityQueue is not really efficient we could heapify in linear time
			ins.offer(i.start);
			outs.offer(i.end);
		}
		
		int over = -1;
		
		while (!ins.isEmpty() && !outs.isEmpty()) {
			if (ins.peek() < outs.peek()) {
				over++; // we have a new coming in
				if (over > max_over) max_over=over;
				ins.remove();
			} else if (ins.peek() > outs.peek()){
				outs.remove();
				over--;
			} else {
				// they are equal - nothing happens
				ins.remove();
				outs.remove();
			}
		}
		
		return max_over;
		
	}
	

	public static void main(String[] args) {
		Interval[] ix = new Interval[200];
		Random r = new Random();
		for (int i = 0; i < ix.length; i++) {
			int start = r.nextInt(1000);
			int end = start + 1 + r.nextInt(10);
			ix[i] = new Interval(start, end);
		}

		Interval[] io = ix.clone();
		
		Arrays.sort(ix, Interval.compareByStart);
		dump(ix);

		boolean hasOverlap = hasOverlap(ix);
		System.out.println(hasOverlap);
		
		int overlap = maxOverlap(ix);
		System.out.println("Max " + overlap + " overlaps");
		int overlap2 = maxOverlapUnsorted(io);
		System.out.println("Max without sort " + overlap2 + " overlaps");
		
		Interval[] merged = mergeAllOverlapsWithStack(ix);		
		dump(merged);	
		
		Interval[] merged2 = mergeUnsorted(io);		
		dump(merged2);	
		
	}

}
