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
package ch.bfh.unicrypt.helper.aggregator.interfaces;

import ch.bfh.unicrypt.helper.tree.Tree;

/**
 * The purpose of an aggregator is to aggregate the values stored in a {@link Tree} of type {@code V} into a single
 * value of type {@code V}. An aggregator is thus required each time the method {@link Tree#aggregate(Aggregator)} is
 * called. For this, each aggregator provides two distinct operations: the conversion of the value stored in some leaf
 * of the tree and the conversion of the values obtained from the children of some node in the tree.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values precessed by the aggregator
 * @see Tree
 */
public interface Aggregator<V> {

	/**
	 * Performs the aggregation of a single value, if necessary (in some cases single values need not to be processed).
	 * <p>
	 * @param value The given value
	 * @return The aggregated value
	 */
	public V aggregate(V value);

	/**
	 * Performs the aggregation of multiple values given as a Java array.
	 * <p>
	 * @param values The given values
	 * @return The aggregated value
	 */
	public V aggregate(V... values);

	/**
	 * Performs the aggregation of multiple values given as an iterable collection.
	 * <p>
	 * @param values The given iterable collection of values
	 * @return The aggregated value
	 */
	public V aggregate(Iterable<V> values);

}
