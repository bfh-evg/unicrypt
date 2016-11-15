/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
 * value of type {@code V}, and vice versa, to construct a {@link Tree} of type {@code V} from a single value of type
 * {@code V}. Each aggregator defines therfore a bijective mapping between type {@link Tree}{@code <V>} and {@code V}.
 * To perform the conversion forth and back, each concrete aggregator class implements two distinct pairs of operations
 * for the conversion of the value stored in some leaf of the tree and the conversion of the aggregated values obtained
 * from the children of some node in the tree.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values processed by the aggregator
 * @see Tree
 */
public interface Aggregator<V> {

	/**
	 * Aggregates a given tree into a single value of type {@code V}.
	 * <p>
	 * @param tree The given tree
	 * @return The resulting value
	 */
	public V aggregate(Tree<V> tree);

	/**
	 * Constructs a new tree from an aggregated value. This method is the inverse of
	 * {@link Aggregator#aggregate(Tree)}.Throws an exception, if the construction of the tree fails for the given
	 * input.
	 * <p>
	 * @param value The aggregated value
	 * @return The new tree
	 */
	public Tree<V> disaggregate(V value);

}
