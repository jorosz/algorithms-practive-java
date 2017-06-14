# Coding practice for various algorithms in Java

## Cars
This is a solution to the following car race problem: [http://www.geeksforgeeks.org/google-interview-question-for-java-position/]

## Fibo
The infamous Fibonacci problem solved many ways
* The naive recursive algorithm. Classroom example of exponential runtime.
* Mathematical calculation for the n'th fibonacci number using Binet's formula. 
  Clearly doesn't work well because of the poor precision of Java's built in maths.
* The linear algorithm which keeps track of the last two numbers.
* A computation of the n'th Fibonacci number using a matrix and leveraging the identity of `{ { Fn+1 Fn } , { Fn, Fn-1} } == { { 1, 1 },{1, 0}} ^ n`
* The matrix algorithm improved with fast exponentionalization based on A^2, A^4, A^8, etc. This in O(log n) and the fastest algorithm for computing Fibo numbers.

## PermuteNumbers
An interesting study in permuting numbers, shuffling an array randomly with uniform distribution (all permutations equally likely) using random numbers.

Also an algorithm that maps a specific a permutation of an array using an 'index' so that you can create all permutations of the array using this index. By iterating the index
it's possible to generate all permutations.

A solution to generate (proper) lottery numbers using a *single* random number.

## PermuteString
Another classic interview problem. Given an input string in the form of 'aaa*b*d' where * is a placeholder produce all possible 
strings where every * would be replaced with all permutations of a given set of characters.

## AQueue/BQueue
Implementation of a queue using an array but with the added spice that it also optimizes the size of the array by 'rolling over' the index
so that the array is not extended until the queue is full.

`AQueue` uses two variables `start` and `last`, each increasing all the time (`last` upon insert, `start` upon removal). The array extends when
`last - start` is larger than the size of the array, otherwise it rolls over (next element goes to index 0). 

Caveat of this approach is that the `int` values of `start` and `last` only increase so the int may wrap at one point breaking the code.

`BQueue` improves `AQueue` by using a varaibles for `offset` and `size` which aren't prone to `int` overrun. `offset` points to the index for
the start and size is the number of elements in the queue.

## SortSample
Sorting of arrays. Classic. Heapsort, quicksort, mergesort all written from scratch.

## NthElement
Linear solutions to identify the n'th element of an array (without sorting the array). These algorithms rely on partitioning the array
based on pivot, identifying the location of the pivot and than 'dividing' the search depending on the location of the pivot (left if pivot
is larger than n, right subarray if pivot is smaller).

`nthof` picks the pivot as a random index which is very effective on average but can be quadratic runtime if the pivot is always picked badly.

`supernthof` picks the pivot using a selection algorithm as the median of medians (groups of 5) which is guaranteed to be larger than 1/4 of the elements
and smaller than 1/4 the elements. It's more effective since the pivot will always eliminate at least 1/4 of the array, however takes time to pick
the pivot.

Ideal case for `nthof` when pivot always cuts in half is T(n) = n (partition) + T(n/2)  = n + n/2 + T(n/4) = n(1+1/2+1/4+1/8...) < 2n (the sum is the so called harmonic number which is smaller than 2)

Worst case for `supernthof` T(n) = n (partition) + n (medians of 5) + T(n/5) (median of medians) + T(3/4n) <= 44n (without proof).

Also has two partitioning algorithms, both linear O(n). `partitionTrivial` scans all elements left to right and moves elements smaller than the pivot to left.
`partitionFast` converges from both the left and the right and swaps left & right values if they are incorrectly positioned. All partitioning
manages duplicate occurances of the pivot and swap the pivot into it's place.

##Intervals
Solutions to classic interval like problems. Find if a list of intervals overlap, find the number of maximum number of overlapping intervals, merge overlapping intervals into one. 
It has algorithms that require the interval to be sorted but also ones which don't require the intervals to be sorted - the latter never sort the intervals but use a heap.
All processing is linear excluding the cost of sorting.

## WordSearcher
The infamous word search problem: find out if a string is composed of words given in a dictionary. Classic dynamic programming problem.
* The naive recursive algorithm which is quite effective, however can have an exponential runtime for the worst case where the dictionary words are
	all prefixes of each other and string we are looking for starts with the longest word. For example: dictionary = { a, aa, aaa, aaaa, aaaaa...}
	
  Worst case the runtime is T(n) = 1 + T(n-1) + T(n-2) + T(n-3)... /expand T(n-1)/ = 1 + 1 + 2T(n-2) + 2T(n-3) + 2T(n-4)... ==> A metric series adding up to (n + 2^n) so exponential

* The memoized version which makes the solution quadratic (assuming memo lookups being "free" and so is recursion since it still calls itself exponential times)

* A bottom-up table based version which is also quadratic. The table based algorithm also has a variant that gives all possible strings split into words.




