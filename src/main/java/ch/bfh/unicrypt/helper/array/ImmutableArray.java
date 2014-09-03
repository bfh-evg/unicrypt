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
package ch.bfh.unicrypt.helper.array;

import ch.bfh.unicrypt.helper.compound.Compound;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class ImmutableArray<T>
	   extends AbstractArray<ImmutableArray<T>>
	   implements Iterable<T>, Compound<ImmutableArray<T>, T>, Array<ImmutableArray<T>, T> {

	// The obects are stored either as an ordinary, possibly empty array (case 1)
	// or as an array of length 1 together with the full length of the immutable
	// array (case 2, all elements are equal)
	private final Object[] array;
	protected Boolean uniform = null;

	protected ImmutableArray(T object, int length) {
		this(new Object[]{object}, 0, length, false);
		this.uniform = true;
	}

	protected ImmutableArray(Object[] objects) {
		this(objects, 0, objects.length, false);
	}

	protected ImmutableArray(Object[] objects, int offset, int length, boolean reverse) {
		super(length, offset, reverse);
		this.array = objects;
	}

	@Override
	public int getArity() {
		return this.length;
	}

	@Override
	public boolean isUniform() {
		if (this.uniform == null) {
			this.uniform = true;
			if (this.length > 1) {
				T first = this.abstractGetAt(0);
				for (int i = 1; i < this.length; i++) {
					if (!first.equals(this.abstractGetAt(i))) {
						this.uniform = false;
						break;
					}
				}
			}
		}
		return this.uniform;
	}

	@Override
	public T getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.abstractGetAt(index);
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
	public Object[] getAll() {
		Object[] result = new Object[this.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	@Override
	public ImmutableArray<T> insertAt(int index, T newObject) {
		if (index < 0 || index > this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (newObject == null) {
			throw new IllegalArgumentException();
		}
		Object[] result = new Object[this.length + 1];
		for (int i = 0; i < result.length; i++) {
			if (i != index) {
				result[i] = (i < index) ? this.abstractGetAt(i) : this.abstractGetAt(i - 1);
			}
		}
		result[index] = newObject;
		return new ImmutableArray<T>(result);
	}

	@Override
	public ImmutableArray<T> replaceAt(int index, T newObject) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (newObject == null) {
			throw new IllegalArgumentException();
		}
		if (this.getAt(index).equals(newObject)) {
			return this;
		}
		Object[] result = new Object[this.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		result[index] = newObject;
		return new ImmutableArray<T>(result);
	}

	@Override
	public ImmutableArray<T> add(T object) {
		return this.insertAt(this.length, object);
	}

	@Override
	public ImmutableArray<T> append(Compound<ImmutableArray<T>, T> compound) {
		return this.append((ImmutableArray<T>) compound);
	}

	@Override
	public ImmutableArray<T> reverse() {
		return new ImmutableArray<T>(this.array, this.offset, this.length, !this.reverse);
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
				return abstractGetAt(currentIndex++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	protected String defaultToStringName() {
		return "";
	}

	@Override
	protected String defaultToStringValue() {
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
		hash = 43 * hash + this.length;
		for (T object : this) {
			hash = 43 * hash + object.hashCode();
		}
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
			if (!this.abstractGetAt(i).equals(other.abstractGetAt(i))) {
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
		return new ImmutableArray<T>(Arrays.copyOf(array, array.length));
	}

	public static <T> ImmutableArray<T> getInstance(Collection<T> collection) {
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		for (T object : collection) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
		}
		return new ImmutableArray<T>(collection.toArray());
	}

	public static <T> ImmutableArray<T> getInstance(T object, int length) {
		if (object == null || length < 0) {
			throw new IllegalArgumentException();
		}
		return new ImmutableArray(new Object[]{object}, 0, length, false);
	}

	@Override
	protected ImmutableArray<T> abstractExtract(int offset, int length) {
		if (this.reverse) {
			offset = this.length - (offset + length);
		}
		return new ImmutableArray<T>(this.array, this.offset + offset, length, this.reverse);
	}

	@Override
	protected ImmutableArray<T> abstractAppend(ImmutableArray<T> other) {
		Object[] result = new Object[this.length + other.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		for (int i = 0; i < other.length; i++) {
			result[this.length + i] = other.abstractGetAt(i);
		}
		return new ImmutableArray<T>(result);
	}

	@Override
	protected Class getArrayClass() {
		return ImmutableArray.class;
	}

	@Override
	public ImmutableArray<T> append(Array<ImmutableArray<T>, T> other) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private T abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		return (T) this.array[(this.offset + index) % this.array.length];
	}

}
