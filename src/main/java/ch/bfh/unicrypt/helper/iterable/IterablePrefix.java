/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
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

import ch.bfh.unicrypt.UniCrypt;
import java.util.Iterator;

/**
 * This generic class selects the first {@code n} elements (prefix) from a given iterable collection of values of type
 * {@code V}. The result is a new iterable collection of values of type {@code V}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the iterable collection
 */
public class IterablePrefix<V>
	   extends UniCrypt
	   implements Iterable<V> {

	private final Iterable<V> iterable;
	private final int maxCounter;

	protected IterablePrefix(Iterable<V> iterable, int maxCounter) {
		this.maxCounter = maxCounter;
		this.iterable = iterable;
	}

	@Override
	public Iterator<V> iterator() {
		return new Iterator<V>() {

			private final Iterator<V> iterator = iterable.iterator();
			private int counter = 0;

			@Override
			public boolean hasNext() {
				return this.counter < maxCounter && this.iterator.hasNext();
			}

			@Override
			public V next() {
				this.counter = this.counter + 1;
				return this.iterator.next();
			}

		};
	}

	/**
	 *
	 * @param <V>      The generic type of the iterable collection
	 * @param iterable The given iterable collection
	 * @param n        The number of elements in the new iterable collection
	 * @return The new iterable collection
	 */
	public static <V> IterablePrefix<V> getInstance(Iterable<V> iterable, int n) {
		if (iterable == null || n < 0) {
			throw new IllegalArgumentException();
		}
		return new IterablePrefix<>(iterable, n);
	}

}
