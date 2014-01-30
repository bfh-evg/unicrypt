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

import ch.bfh.unicrypt.math.helper.compound.Compound;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class ImmutableArray<T>
	   extends UniCrypt
	   implements Iterable<T>, Compound<ImmutableArray<T>, T> {

	// The obects are stored either as an ordinary, possibly empty array (case 1)
	// or as an array of length 1 together with the full length of the immutable
	// array (case 2, all elements are equal)
	private final T[] array;
	private final int length;

	// Case 1: General case
	private ImmutableArray(T[] array) {
		this.array = array;
		this.length = array.length;
	}

	// Case 2: Special constructor for array of length 1
	private ImmutableArray(T object) {
		this(object, 1);
	}

	// Case 2: General case
	private ImmutableArray(T object, int length) {
		this.array = (T[]) Array.newInstance(object.getClass(), 1);
		this.array[0] = object;
		this.length = length;
	}

	public int getLength() {
		return this.length;
	}

	@Override
	public int getArity() {
		return this.length;
	}

	@Override
	public boolean isUniform() { // case 2 or empty
		return this.array.length <= 1;
	}

	@Override
	public boolean isEmpty() {
		return this.length == 0;
	}

	@Override
	public T getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) { // case 2, not empty
			return this.array[0];
		}
		return this.array[index]; // case 1
	}

	@Override
	public T getFirst() {
		return this.getAt(0);
	}

	@Override
	public T getLast() {
		return this.getAt(this.length - 1);
	}

	@Override
	public T[] getAll() {
		T[] result = Arrays.copyOf(this.array, this.length);
		if (this.isUniform() && !this.isEmpty()) { // case 2, not empty
			Arrays.fill(result, this.array[0]);
		}
		return result;
	}

	@Override
	public ImmutableArray<T> removeAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isUniform()) { // case 2, not empty
			if (this.length == 1) {
				return new ImmutableArray<T>(Arrays.copyOf(this.array, 0));
			} else {
				return new ImmutableArray<T>(this.array[0], this.length - 1);
			}
		}
		T[] result = Arrays.copyOf(this.array, this.length - 1);
		for (int i = index; i < this.length - 1; i++) {
			result[i] = this.array[i + 1];
		}
		// calling getInstance is necessary to check whether the new array is uniform
		return ImmutableArray.getInstance(result);
	}

	@Override
	public ImmutableArray<T> insertAt(int index, T object) {
		if (index < 0 || index > this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (object == null) {
			throw new IllegalArgumentException();
		}
		if (this.isUniform()) { // case 2, possibly empty
			if (this.isEmpty() || this.array[0].equals(object)) {
				return new ImmutableArray<T>(object, this.length + 1);
			}
			T[] result = Arrays.copyOf(this.array, this.length + 1);
			Arrays.fill(result, this.array[0]);
			result[index] = object;
			return new ImmutableArray<T>(result);
		}
		T[] result = Arrays.copyOf(this.array, this.length + 1);
		for (int i = index + 1; i < result.length; i++) {
			result[i] = this.array[i - 1];
		}
		result[index] = object;
		return new ImmutableArray<T>(result);
	}

	@Override
	public ImmutableArray<T> replaceAt(int index, T object) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (object == null) {
			throw new IllegalArgumentException();
		}
		if (this.getAt(index).equals(object)) {
			return this;
		}
		T[] result = Arrays.copyOf(this.array, this.length);
		if (this.isUniform()) { // case 2, not empty
			Arrays.fill(result, this.array[0]);
		}
		result[index] = object;
		return new ImmutableArray<T>(result);
	}

	@Override
	public ImmutableArray<T> add(T object) {
		return this.insertAt(this.length, object);
	}

	@Override
	public ImmutableArray<T> append(Compound<ImmutableArray<T>, T> compound) {
		if (compound == null) {
			throw new IllegalArgumentException();
		}
		ImmutableArray<T> other = (ImmutableArray<T>) compound;
		if (this.isEmpty()) {
			return other;
		}
		if (other.isEmpty()) {
			return this;
		}
		if (this.isUniform() && other.isUniform() && this.getFirst().equals(other.getFirst())) {
			return new ImmutableArray<T>(this.getFirst(), this.length + other.length);
		}
		T[] newArray = (T[]) Array.newInstance(this.array.getClass().getComponentType(), this.length + other.length);
		for (int i = 0; i < this.length; i++) {
			newArray[i] = this.getAt(i);
		}
		for (int i = 0; i < other.length; i++) {
			newArray[this.length + i] = other.getAt(i);
		}
		return new ImmutableArray<T>(newArray);
	}

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
	public String standardToStringContent() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.length; i++) {
			str = str + delimiter + this.getAt(i);
			delimiter = ", ";
		}
		return str;
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
		boolean isUniform = true;
		for (T object : array) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
			isUniform = isUniform && object.equals(array[0]);
		}
		if (isUniform && array.length != 0) {
			return new ImmutableArray(array[0], array.length);
		}
		// Array.copyOf is necessary to protect external array modification
		return new ImmutableArray<T>(Arrays.copyOf(array, array.length));
	}

	public static <T> ImmutableArray<T> getInstance(T object, int length) {
		if (object == null || length < 1) {
			throw new IllegalArgumentException();
		}
		return new ImmutableArray(object, length);
	}

	// DOES NOT WORK IN JAVA6
//	// used for casting
//	public static <S extends T, T> ImmutableArray<S> getInstance(ImmutableArray<T> oldArray, Class<S> newType) {
//		if (oldArray.isUniform() && !oldArray.isEmpty()) {
//			return new ImmutableArray((S) oldArray.getFirst(), oldArray.getLength());
//		}
//		S[] newArray = (S[]) Array.newInstance(newType, oldArray.length);
//		for (int i = 0; i < oldArray.length; i++) {
//			newArray[i] = (S) oldArray.getAt(i);
//		}
//		return new ImmutableArray(newArray);
//	}
}
