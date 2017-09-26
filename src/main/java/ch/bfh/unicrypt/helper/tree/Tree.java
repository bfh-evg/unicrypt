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
package ch.bfh.unicrypt.helper.tree;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * This is an abstract base class for representing trees containing values of a generic type {@code V} in their leaves.
 * A tree is either a {@link Node} containing other trees as children, or a {@link Leaf} containing a value. Using an
 * instance of {@link Aggregator} of the same type {@code V}, a tree can be aggregated into a single value of type
 * {@code V} (and back). This conversion is the main functionality of this class. Additionally, trees are iterable over
 * the values stored in the leaves (in a pre-order fashion).
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values stored in the tree
 * @see Node
 * @see Leaf
 */
public abstract class Tree<V>
	   extends UniCrypt
	   implements Iterable<V> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new leaf storing a given value.
	 * <p>
	 * @param <V>   The generic type of the given value and the resulting leaf
	 * @param value The given value
	 * @return The new leaf
	 */
	public static <V> Leaf<V> getInstance(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		return new Leaf<>(value);
	}

	/**
	 * Creates a new node from a given array of values (its children) of type {@code V}.
	 * <p>
	 * @param <V>    The generic type of the tree
	 * @param values The given array of values
	 * @return The new node
	 */
	public static <V> Node<V> getInstance(V... values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		Leaf<V>[] leafs = new Leaf[values.length];
		for (int i = 0; i < values.length; i++) {
			leafs[i] = getInstance(values[i]);
		}
		return getInstance(leafs);
	}

	/**
	 * Creates a new node from a single sub-tree (its child) of type {@code V}.
	 * <p>
	 * @param <V>   The generic type of the tree
	 * @param child The given sub-tree
	 * @return The new node
	 */
	public static <V> Node<V> getInstance(Tree<V> child) {
		if (child == null) {
			throw new IllegalArgumentException();
		}
		return Tree.getInstance(Sequence.getInstance(child));
	}

	/**
	 * Creates a new node from a given array of sub-trees (its children) of type {@code V}. This is a convenience method
	 * for {@link Node#getInstance(Sequence)}.
	 * <p>
	 * @param <V>      The generic type of the tree
	 * @param children The given array of sub-trees
	 * @return The new node
	 */
	public static <V> Node<V> getInstance(Tree<V>... children) {
		return Tree.getInstance(Sequence.getInstance(children));
	}

	/**
	 * Creates a new node from a given sequence of sub-trees (its children) of type {@code V}.
	 * <p>
	 * @param <V>      The generic type of the tree
	 * @param children The given sub-trees
	 * @return The new node
	 */
	public static <V> Node<V> getInstance(Sequence<Tree<V>> children) {
		if (children == null) {
			throw new IllegalArgumentException();
		}
		return new Node<>(children.filter(Sequence.NOT_NULL));
	}

	/**
	 * Creates a new tree of type {@code V} from an aggregated value. This method is the inverse of
	 * {@link Tree#aggregate(Aggregator)}. Throws an exception, if the construction of the tree fails for the given
	 * input. Calling this method is identical to calling {@link Aggregator#disaggregate(Object)}.
	 * <p>
	 * @param <V>             The generic type of the given aggregated value and the resulting tree
	 * @param aggregatedValue The aggregated value
	 * @param aggregator      The aggregator specifying the conversion.
	 * @return The new tree
	 */
	public static <V> Tree<V> getInstance(V aggregatedValue, Aggregator<V> aggregator) {
		if (aggregatedValue == null || aggregator == null) {
			throw new IllegalArgumentException();
		}
		return aggregator.disaggregate(aggregatedValue);
	}

	/**
	 * Aggregates the values stored in the leaves of the tree into a single value of type {@code V}. Calling this method
	 * is identical to calling {@link Aggregator#aggregate(Tree)}.
	 * <p>
	 * @param aggregator The aggregator specifying the conversion.
	 * @return The aggregated value
	 */
	public final V aggregate(Aggregator<V> aggregator) {
		if (aggregator == null) {
			throw new IllegalArgumentException();
		}
		return aggregator.aggregate(this);
	}

	/**
	 * Checks if the tree is a leaf.
	 * <p>
	 * @return {@code true}, if the tree is a leaf, {@code false} otherwise
	 */
	public boolean isLeaf() {
		return this instanceof Leaf;
	}

	/**
	 * Checks if the tree is a node (not a leaf).
	 * <p>
	 * @return {@code true}, if the tree is a node, {@code false} otherwise
	 */
	public boolean isNode() {
		return this instanceof Node;
	}

	public abstract Sequence<V> getSequence();

	@Override
	protected String defaultToStringType() {
		return "Tree";
	}

	@Override
	protected String defaultToStringContent() {
		return "";
	}

}
