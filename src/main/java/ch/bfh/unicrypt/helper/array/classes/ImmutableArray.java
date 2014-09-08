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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.array.abstracts.AbstractArray;
import java.util.Collection;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class ImmutableArray<T extends Object>
	   extends AbstractArray<ImmutableArray<T>, T> {

	// The obects are stored either as an ordinary, possibly empty array (case 1)
	// or as an array of length 1 together with the full length of the immutable
	// array (case 2, all elements are equal)
	private final Object[] array;

	protected ImmutableArray(T object, int length) {
		this(new Object[]{object}, 0, length, false);
	}

	protected ImmutableArray(Object[] objects) {
		this(objects, 0, objects.length, false);
	}

	protected ImmutableArray(Object[] objects, int offset, int length, boolean reverse) {
		super(length, offset, reverse);
		this.array = objects;
		if (objects.length <= 1) {
			this.uniform = true;
		}
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
			str = str + delimiter + this.abstractGetAt(i);
			delimiter = ", ";
		}
		return "[" + str + "]";
	}

	public static <T> ImmutableArray<T> getInstance(T... array) {
		if (array == null) {
			throw new IllegalArgumentException();
		}
		Object[] newArray = new Object[array.length];
		int i = 0;
		for (T object : array) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
			newArray[i++] = object;
		}
		return new ImmutableArray<T>(newArray);
	}

	public static <T> ImmutableArray<T> getInstance(Collection<T> collection) {
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		Object[] array = new Object[collection.size()];
		int i = 0;
		for (T object : collection) {
			if (object == null) {
				throw new IllegalArgumentException();
			}
			array[i++] = object;
		}
		return new ImmutableArray<T>(array);
	}

	public static <T> ImmutableArray<T> getInstance(T object, int length) {
		if (object == null || length < 0) {
			throw new IllegalArgumentException();
		}
		return new ImmutableArray(new Object[]{object}, 0, length, false);
	}

	@Override
	protected T abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		return (T) this.array[(this.offset + index) % this.array.length];
	}

	@Override
	protected ImmutableArray<T> abstractExtract(int fromIndex, int length) {
		if (this.reverse) {
			fromIndex = this.length - fromIndex - length;
		}
		return new ImmutableArray<T>(this.array, this.offset + fromIndex, length, this.reverse);
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
	protected ImmutableArray<T> abstractInsertAt(int index, T newObject) {
		Object[] result = new Object[this.length + 1];
		for (int i = 0; i < this.length; i++) {
			if (i < index) {
				result[i] = this.abstractGetAt(i);
			} else {
				result[i + 1] = this.abstractGetAt(i);
			}
		}
		result[index] = newObject;
		return new ImmutableArray<T>(result);
	}

	@Override
	protected ImmutableArray<T> abstractReplaceAt(int index, T newObject) {
		Object[] result = new Object[this.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		result[index] = newObject;
		return new ImmutableArray<T>(result);
	}

	@Override
	protected ImmutableArray<T> abstractReverse() {
		return new ImmutableArray<T>(this.array, this.offset, this.length, !this.reverse);
	}

	@Override
	protected Class getArrayClass() {
		return ImmutableArray.class;
	}

}
