package com.example.fibo;

import java.util.function.Function;

import com.example.util.Measured;

/** Different ways to calculate the fibonacci numbers.
 * 
 * Note: long cannot store the fibonacci numbers larger than the 92nd number but these algorithms would work beyond that using a type with higher precision.
 * 
 * @author jozseforosz
 *
 */
public class Fibo extends Measured {

	/** The naive recursive algorithm. Classroom example of exponential runtime of course.
	 * 
	 */
	public static long getFiboNaive(int n) {
		assert n > 0;
		if (n == 0)
			return 0;
		if (n == 1 || n == 2)
			return 1;
		return getFiboNaive(n - 1) + getFiboNaive(n - 2);
	}

	private static final double sq5 = Math.sqrt(5.0);
	private static final double fi = sq5 / 2 + 0.5;

	/** Mathematical calculation for the n'th fibonacci number.
	 * fib(n) = round( fi ^ n / sqrt(5)) where fi is the golden ratio
	 * 
	 * Note that Java math is simply not precise enough to calculate larger numbers here.
	 * 
	 */
	public static long getFiboWithMath(int n) {
		return Math.round(Math.pow(fi, n) / sq5);
	}

	/** Linear algorithm to calculate the n'th fibonacci number */
	public static long getFiboWithLinear(int n) {
		if (n == 0)
			return 0;

		long last = 1;
		long second = 0;

		for (int i = 2; i <= n; i++) {
			long current = last + second;
			second = last;
			last = current;
			
			if (Long.MAX_VALUE - last < second) System.err.println("We'll overrun next time at index "+i);
		}

		return last;
	}

	/** Helper function to multiply a 2x2 matrix of longs */
	private static long[][] mmul(long A[][], long B[][]) {
		return new long[][] { { A[0][0] * B[0][0] + A[0][1] * B[1][0], A[0][0] * B[0][1] + A[0][1] * B[1][1] },
				{ A[1][0] * B[0][0] + A[1][1] * B[1][0], A[1][0] * B[0][1] + A[1][1] * B[1][1] } };
	}


	/** Computes the n'th Fibonacci number with matrix algoritm using
	 * simple multiplication. Fib(n) is  in the top left corner of 
	 * A^(n-1) where A is the matrix { {1,1}, {1,0}}
	 * 
	 */
	public static long getFiboWithMatrix(int n) {
		long[][] A = { { 1, 1 }, { 1, 0 } };
		long[][] R = { { 1, 0 }, { 0, 1 } }; // start with identity
		for (int i = 1; i <= n - 1; i++) {
			R = mmul(R, A);
		}
		return R[0][0];
	}

	/** Computes the n'th Fibonacci number with matrix algoritm as before but using
	 * fast exponentional calculation where A^n is calculated with A^2, A^4, A^8, etc.
	 */
	public static long getFiboWithMatrixLog(int n) {
		long[][] A = { { 1, 1 }, { 1, 0 } };
		long[][] R = { { 1, 0 }, { 0, 1 } };

		for (int i = 1; i <= n - 1; i *= 2) {
			// A now has A^i
			if (((n - 1) & i) == i) {
				// if n has bit i set then this must be applied in the result
				R = mmul(R, A);
			}
			A = mmul(A, A); // calculate next A value A = A^i^2 ( A^2, A^4, A^8,
							// ..)
		}
		// We have A^n-1 which has F(n) in [0][0]
		return R[0][0];
	}



	public static void main(String[] args) {
		measure("with math",Fibo::getFiboWithMath, 92);
		measure("naive",Fibo::getFiboNaive, 90);
		measure("linear",Fibo::getFiboWithLinear, 92);
		measure("matrix multiply",Fibo::getFiboWithMatrix, 92);
		measure("matrix fast exp",Fibo::getFiboWithMatrixLog, 92);
	}

}
