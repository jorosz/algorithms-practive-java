package com.example.queue;

import java.util.Arrays;

/** Implementation of a queue using an array which supports 'rollover' in order to guarantee the 
 * maximal use of the array. In case the queue length doesn't exceed the length of the queue the array inside 
 * does not extend but values simply 'roll over' to the start of the array until all space in the array is used.
 * The array will extend in size when the queue size limit is reached.
 * 
 * This implementation uses two variables 'offset' and 'size' which give protection from the overrun of
 * these variables since offset is always less than the size of the array and size is the number of elements in the queue.
 * 
 * @author jozseforosz
 *
 * @param <E>
 */
public class BQueue<E> {

	private Object[] elements;
	private int offset;
	private int size;

	public BQueue(int size) {
		this.elements = new Object[size];
	}

	public void enqueue(E elem) {
		if (size == elements.length) {
			System.out.println("FULL! Extending");
			Object[] new_elements = new Object[elements.length * 2];

			System.arraycopy(elements, offset, new_elements, 0, elements.length - offset);
			System.arraycopy(elements, 0, new_elements, elements.length - offset, offset);

			System.out.println(Arrays.toString(elements));
			System.out.println(Arrays.toString(new_elements));

			this.elements = new_elements;
			this.offset = 0;
		}

		int idx = (offset + size++) % elements.length;
		elements[idx] = elem;
		System.out.println("Insert " + elem + " @index " + idx + " offset " + offset + " size " + size);
	}

	public E dequeue() {
		if (size > 0) {
			@SuppressWarnings("unchecked")
			E e = (E) elements[offset];
			
			size--;
			offset = (offset + 1) % elements.length;
			return e;
		} else {
			System.out.println("EMPTY!");
			return null;
		}
	}
	
	public void push(E elem) {
		enqueue(elem);
	}
	
	public E pop() {
		if (size > 0) {
			@SuppressWarnings("unchecked")
			E e = (E) elements[(offset + --size) % elements.length];
			return e;
		} else {
			System.out.println("EMPTY!");
			return null;			
		}
	}
	
	public void insert(E elem) {
		if (size == elements.length) {
			System.out.println("FULL! Extending");
			Object[] new_elements = new Object[elements.length * 2];

			System.arraycopy(elements, offset, new_elements, 1, elements.length - offset);
			System.arraycopy(elements, 0, new_elements, elements.length - offset + 1, offset);

			System.out.println(Arrays.toString(elements));
			System.out.println(Arrays.toString(new_elements));

			this.elements = new_elements;
			this.offset = 1;
		}

		offset = (offset - 1 + elements.length) % elements.length;
		size++;
		elements[offset] = elem;
		System.out.println("Insert " + elem + " @ index " + offset + " offset " + offset + " size " + size);
	}
	
	

	public static void main(String[] args) {
		BQueue<Integer> q = new BQueue<Integer>(4);
		q.enqueue(1);
		System.out.println(q.dequeue());
		q.enqueue(2);
		q.enqueue(3);
		q.enqueue(4);
		System.out.println(q.pop());
		q.insert(100);
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		q.enqueue(5);
		q.insert(200);
		q.enqueue(6);
		q.enqueue(7);
		q.enqueue(8);
		q.enqueue(9);
		q.enqueue(10);
		q.enqueue(11);
		q.enqueue(12);
		q.enqueue(13);
		q.enqueue(14);
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
		System.out.println(q.dequeue());
	}

}
