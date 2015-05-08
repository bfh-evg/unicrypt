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
package ch.bfh.unicrypt.helper.aggregator;

import ch.bfh.unicrypt.helper.aggregator.interfaces.InvertibleAggregator;

/**
 * Instances of this class are containers for storing either a single value or an iterable collection of values of a
 * generic type {@code V}. The main usage of this class is the return value of the method
 * {@link InvertibleAggregator#disaggregate(java.lang.Object)}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values
 */
public class SingleOrMultiple<V> {

	private final V value;
	private final Iterable<V> values;

	private SingleOrMultiple(V value) {
		this.value = value;
		this.values = null;
	}

	private SingleOrMultiple(Iterable<V> values) {
		this.value = null;
		this.values = values;
	}

	/**
	 * Creates a new instance from a single value.
	 * <p>
	 * @param <V>   The generic type of the value
	 * @param value The given value
	 * @return The new instance
	 */
	public static <V> SingleOrMultiple<V> getInstance(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return new SingleOrMultiple<V>(value);
	}

	/**
	 * Creates a new instance from an iterable collection of values.
	 * <p>
	 * @param <V>    The generic type of the values
	 * @param values The given iterable collection of values
	 * @return The new instance
	 */
	public static <V> SingleOrMultiple<V> getInstance(Iterable<V> values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		return new SingleOrMultiple<V>(values);
	}

	/**
	 * Checks if the instance contains a single value or multiple values.
	 * <p>
	 * @return {@literal true}, if the instance contains a single value, {@literal false} otherwise
	 */
	public boolean isSingle() {
		return (this.values == null);
	}

	/**
	 * Return the single value (if one exists).
	 * <p>
	 * @return The single value
	 */
	public V getValue() {
		if (this.value == null) {
			throw new UnsupportedOperationException();
		};
		return this.value;
	}

	/**
	 * Returns the iterable collection of values (if one exists).
	 * <p>
	 * @return The iterable collection
	 */
	public Iterable<V> getValues() {
		if (this.values == null) {
			throw new UnsupportedOperationException();
		};
		return this.values;
	}

}
