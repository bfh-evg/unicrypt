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
package ch.bfh.unicrypt.helper.aggregator.abstracts;

import ch.bfh.unicrypt.helper.UniCrypt;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.iterable.IterableMapper;
import ch.bfh.unicrypt.helper.iterable.Mapper;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;

/**
 * This abstract class serves as a base implementation for the {@link Aggregator} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values precessed by the aggregator
 */
public abstract class AbstractAggregator<V>
	   extends UniCrypt
	   implements Aggregator<V> {

	private final Mapper<Tree<V>, V> mapper1;
	private final Mapper<V, Tree<V>> mapper2;
	private final Class<V> aggregatorClass;

	protected AbstractAggregator(Class<V> aggregatorClass) {
		mapper1 = new Mapper<Tree<V>, V>() {

			@Override
			public V map(Tree<V> tree) {
				return aggregate(tree);
			}

		};
		this.mapper2 = new Mapper<V, Tree<V>>() {

			@Override
			public Tree<V> map(V value) {
				return disaggregate(value);
			}

		};
		this.aggregatorClass = aggregatorClass;
	}

	@Override
	public Class<V> getAggregatorClass() {
		return this.aggregatorClass;
	}

	public V aggregate(Tree<V> tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}

		// Case 1: tree is a leaf
		if (tree.isLeaf()) {
			Leaf<V> leaf = (Leaf<V>) tree;
			return this.abstractAggregateLeaf(leaf.getValue());
		}

		// Case 2: tree is a node
		final Node<V> node = (Node<V>) tree;
		Iterable<V> values = IterableMapper.getInstance(node.getChildren(), this.mapper1);
		return this.abstractAggregateNode(values, node.getSize());
	}

	@Override
	public Tree<V> disaggregate(final V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}

		// Case 1: value represents a leaf
		if (this.abstractIsLeaf(value)) {
			V result = this.abstractDisaggregateLeaf(value);
			return Leaf.getInstance(result);
		}

		// Case 2: value represents a node
		if (this.abstractIsNode(value)) {
			Iterable<V> values = this.abstractDisaggregateNode(value);
			Iterable<Tree<V>> trees = IterableMapper.getInstance(values, this.mapper2);
			return Node.getInstance(trees);
		}

		// Case 3: value represents not a leaf and not a node
		throw new IllegalArgumentException();
	}

	protected abstract boolean abstractIsLeaf(V value);

	protected abstract boolean abstractIsNode(V value);

	protected abstract V abstractAggregateLeaf(V value);

	protected abstract V abstractAggregateNode(Iterable<V> values, int size);

	protected abstract V abstractDisaggregateLeaf(V value);

	protected abstract Iterable<V> abstractDisaggregateNode(V value);

}