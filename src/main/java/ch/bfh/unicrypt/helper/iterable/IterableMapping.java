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

import java.util.Iterator;

/**
 * This generic class applies a {@link Mapping}{@code <V,W>} to the values of an iterable collection of values of type
 * {@code V}. The result is a new iterable collection of values of type {@code W}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the input iterable collection
 * @param <W> The generic type of this iterable collection
 * @see Mapping
 */
public class IterableMapping<V, W>
	   implements Iterable<W> {

	final private Iterable<V> values;
	final private Mapping<V, W> mapping;

	protected IterableMapping(Iterable<V> values, Mapping<V, W> mapping) {
		this.values = values;
		this.mapping = mapping;
	}

	/**
	 * Returns a new instance of this class, which represents the iterable collection of values obtained by applying a
	 * given mapping to the values of a given iterable collection.
	 * <p>
	 * @param <V>     The generic type of the input iterable collection
	 * @param <W>     The generic type of the new iterable collection
	 * @param values  The input iterable collection
	 * @param mapping The mapping applied to the input values
	 * @return The new iterable collection of type {@code W}
	 */
	public static <V, W> IterableMapping<V, W> getInstance(Iterable<V> values, Mapping<V, W> mapping) {
		if (values == null || mapping == null) {
			throw new IllegalArgumentException();
		}
		return new IterableMapping<V, W>(values, mapping);
	}

	@Override
	public Iterator<W> iterator() {
		return new Iterator<W>() {

			private final Iterator<V> iterator = values.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public W next() {
				return mapping.map(iterator.next());
			}

		};

	}

}
