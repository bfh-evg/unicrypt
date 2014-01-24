/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.math.helper;

import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class ImmutableArray<T>
	   extends UniCrypt
	   implements Iterable<T> {

	// The obects are stored either as an ordinary array (case 1) or as an array
	// of length 1 together with the full array length (case 2)
	private final T[] array;
	private final int length;

	// Case 1
	private ImmutableArray(T[] array) {
		this.array = array.clone();
		this.length = array.length;
	}

	// Case 2
	private ImmutableArray(T[] arrayOfLengthOne, int length) {
		this.array = arrayOfLengthOne;
		this.length = length;
	}

	public int getLength() {
		return this.length;
	}

	public T getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (this.array.length == 1) { // Case 2
			return this.array[0];
		}
		return this.array[index]; // Case1
	}

	public T[] getAll() {
		T[] result = Arrays.copyOf(this.array, this.length);
		if (this.array.length == 1) { // Case 2
			Arrays.fill(result, this.array[0]);
		}
		return result;
	}

//		@Override
//	public ImmutableArray<T> insertAt(int index, T object) {
//		if (index < 0 || index > this.length) {
//			throw new IndexOutOfBoundsException();
//		}
//		if (object == null) {
//			throw new IllegalArgumentException();
//		}
//		if (this.array.length == 1) { // Case 2
//			if (this.array[0].equals(object)) {
//				return ImmutableArray.getInstance(object, this.length+1);
//			}
//		}
//		T[] result = Arrays.copyOf(this.array, this.length + 1);
//		if (this.array.length == 1) { // Case 2
//			for (int i=1; i<this.length; i++) {
//				result.
//			}
//		}
//		result[this.length] = object;
//		return ImmutableArray.getInstance(result);
//
//
//		Element[] newElements = new Element[this.arity + 1];
//		for (int i = 0; i < this.arity + 1; i++) {
//			if (i < index) {
//				newElements[i] = this.getAt(i);
//			} else if (i == index) {
//				newElements[i] = object;
//			} else {
//				newElements[i] = this.getAt(i - 1);
//			}
//		}
//		return this.getSet().insertAt(index, object.getSet()).getElement(newElements);
//	}
//	@Override
//	public Tuple add(Element object) {
//		return this.insertAt(this.arity, object);
//	}
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < length;
			}

			@Override
			public T next() {
				return getAt(currentIndex++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 43 * hash + Arrays.hashCode(this.array);
		hash = 43 * hash + this.length;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		final ImmutableArray<?> other = (ImmutableArray<?>) obj;
		if (this.length != other.length) {
			return false;
		}
		for (int i = 0; i < this.length; i++) {
			if (!this.getAt(i).equals(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static <T> ImmutableArray<T> getInstance(T... array) {
		if (array == null) {
			throw new IllegalArgumentException();
		}
		for (T object : array) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
		}
		return new ImmutableArray<T>(array);
	}

	public static <T> ImmutableArray<T> getInstance(T object, int length) {
		if (object == null || length < 0) {
			throw new IllegalArgumentException();
		}
		return ImmutableArray.<T>getInstance(length, object);
	}

	private static <T> ImmutableArray<T> getInstance(int length, T... arrayOfLengthOne) {
		// the T... parameter helps creating an array of type T
		return new ImmutableArray<T>(arrayOfLengthOne, length);
	}

}
