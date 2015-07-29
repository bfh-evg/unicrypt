/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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

import java.util.Iterator;

/**
 * Instances of this class offer a simple way of creating iterators over a single value.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the value and the iterator
 */
public class IterableValue<V>
	   implements Iterable<V> {

	private final V value;

	private IterableValue(V value) {
		this.value = value;
	}

	/**
	 * Return a new iterator which iterates over the given single value.
	 * <p>
	 * @param <V>   The generic type of the value and the resulting iterator
	 * @param value The given value
	 * @return The iterator over the given value
	 */
	public static <V> IterableValue<V> getInstance(V value) {
		return new IterableValue<>(value);
	}

	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {

			private boolean next = true;

			@Override
			public boolean hasNext() {
				return this.next;
			}

			@Override
			public V next() {
				next = false;
				return value;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
