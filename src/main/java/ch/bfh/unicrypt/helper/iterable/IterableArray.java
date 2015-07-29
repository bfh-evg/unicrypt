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
package ch.bfh.unicrypt.helper.iterable;

import ch.bfh.unicrypt.UniCrypt;
import java.util.Arrays;
import java.util.Iterator;

/**
 * This class is a wrapper class for Java arrays for making them iterable. It thus offers an inexpensive way of creating
 * (finite) iterators over arrays without copying the array elements to a collection. Note that instances of this class
 * are not safe against modifications of the original array after their construction. Also, they do not prevent the
 * original array from containing and the resulting iterators from returning the value {@code null}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <T> The generic type of the elements in the array
 */
public class IterableArray<T>
	   extends UniCrypt
	   implements Iterable<T> {

	private static final long serialVersionUID = 1L;

	private final T[] array;

	private IterableArray(T[] array) {
		this.array = array;
	}

	/**
	 * Creates a new iterable array from a given Java array.
	 * <p>
	 * @param <T>   The generic type of the array
	 * @param array The given array
	 * @return The new iterable array
	 */
	public static <T> IterableArray<T> getInstance(T... array) {
		if (array == null) {
			throw new IllegalArgumentException();
		}
		return new IterableArray<>(array);
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int pos = 0;

			@Override
			public boolean hasNext() {
				return pos < array.length;
			}

			@Override
			public T next() {
				return array[pos++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 3;
		return Arrays.hashCode(array);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IterableArray<?> other = (IterableArray<?>) obj;
		return Arrays.equals(this.array, other.array);
	}

	@Override
	protected String defaultToStringContent() {
		return Arrays.toString(this.array);
	}

}
