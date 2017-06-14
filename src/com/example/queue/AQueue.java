package com.example.queue;

import java.util.Arrays;

/** Implementation of a queue using an array which supports 'rollover' in order to guarantee the 
 * maximal use of the array. In case the queue length doesn't exceed the length of the queue the array inside 
 * does not extend but values simply 'roll over' to the start of the array until all space in the array is used.
 * The array will extend in size when the queue size limit is reached.
 * 
 * This implementation uses two variables 'start' and 'last' and as such is prone to overrun in case of making
 * too many operations since both 'start' and 'last' increase only.
 * 
 * @author jozseforosz
 *
 * @param <E>
 */
public class AQueue<E> {

	private E[] elements;
	private int start;
	private int last;
	
	@SuppressWarnings("unchecked")
	public AQueue(int size) {
		this.elements = (E[]) new Object[size];
	}

	public void add(E elem) {
		if (last > start && start % elements.length == last % elements.length) {
			System.out.println("FULL! Extending");
			
			@SuppressWarnings("unchecked")
			E[] new_elements = (E[]) new Object[elements.length * 2];
			
			System.arraycopy(elements, start % elements.length, new_elements, start % elements.length, elements.length - start % elements.length);
			System.arraycopy(elements, 0, new_elements, elements.length, start % elements.length);

			System.out.println(Arrays.toString(elements));
			System.out.println(Arrays.toString(new_elements));
			
			this.elements = new_elements;
		}
		elements[last++ % elements.length] = elem;
		System.out.println("Start "+start+" end "+last);
	}

	public E get() {
		if (start < last) {
			return elements[start++ % elements.length];
		} else {
			return null;
		}
	}
	
	
	public static void main(String[] args) {
		AQueue<Integer> q = new AQueue<Integer>(5);
		q.add(1);
		q.add(2);
		q.add(3);
		System.out.println(q.get());
		q.add(4);
		System.out.println(q.get());
		System.out.println(q.get());
		q.add(5);
		q.add(6);	
		q.add(7);
		q.add(8);
		q.add(9);
		q.add(10);
		q.add(11);
		q.add(12);
		q.add(13);
		q.add(14);
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());
		System.out.println(q.get());

	}

}
