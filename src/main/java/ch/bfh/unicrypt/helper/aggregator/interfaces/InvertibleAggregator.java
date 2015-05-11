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
 * An invertible aggregator is an aggregator with an additional method
 * {@link InvertibleAggregator#disaggregateLeaf(Object)} for converting an aggregated value back to the original input.
 * The purpose of an invertible aggregator is to help re-constructing a tree from a single aggregated value using
 * {@link Tree#getInstance(Object, InvertibleAggregator)}. In this process, the additional method
 * {@link InvertibleAggregator#disaggregateLeaf(Object)} produces a single value to be stored in a leaf. Similarly, the
 * method {@link InvertibleAggregator#disaggregateNode(Object)} produces multiple values to be processed further to
 * construct the children of a node.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values precessed by the invertible aggregator
 * @see Tree
 * @see SingleOrMultiple
 */
public interface InvertibleAggregator<V>
	   extends Aggregator<V> {

	/**
	 * Inverts the aggregation for a given input value. The result is either a single value or multiple values.
	 * <p>
	 * @param value The given aggregated value
	 * @return The result of inverting the aggregation
	 */
	public V disaggregateLeaf(V value);

	/**
	 * Inverts the aggregation for a given input value. The result is either a single value or multiple values.
	 * <p>
	 * @param value The given aggregated value
	 * @return The result of inverting the aggregation
	 */
	public Iterable<V> disaggregateNode(V value);

	/**
	 * Checks if a given value represents a leaf.
	 * <p>
	 * @param value The given value
	 * @return {@literal true}, if the value represents a leaf, {@literal false} otherwise
	 */
	public boolean isLeaf(V value);

	/**
	 * Checks if a given value represents a node.
	 * <p>
	 * @param value The given value
	 * @return {@literal true}, if the value represents a node, {@literal false} otherwise
	 */
	public boolean isNode(V value);

}
