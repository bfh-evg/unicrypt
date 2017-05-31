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

import ch.bfh.unicrypt.helper.sequence.MultiSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.util.Iterator;

/**
 * An instances of this class represents an internal node of a {@link Tree}. The recursive definition of a tree implies
 * that a node is a tree on its own. In other words, a node connects several sub-trees (its children) to a new tree. The
 * number of children is not restricted and may even be 0.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values stored in the leaves of the tree
 * @see Tree
 * @see Leaf
 */
public class Node<V>
	   extends Tree<V> {

	private static final long serialVersionUID = 1L;

	private final Sequence<Tree<V>> children;

	protected Node(Sequence<Tree<V>> children) {
		this.children = children;
	}

	/**
	 * Returns the sequences of the node's children.
	 * <p>
	 * @return The node's children
	 */
	public Sequence<Tree<V>> getChildren() {
		return this.children;
	}

	/**
	 * Returns the node's number of children.
	 * <p>
	 * @return The number of children
	 */
	public int getSize() {
		return this.children.getLength().intValue();
	}

	@Override
	public Sequence<V> getSequence() {
		return MultiSequence.getInstance(this.children.map(child -> child.getSequence())).flatten();
	}

	@Override
	public Iterator<V> iterator() {
		return this.getSequence().iterator();
	}

	@Override
	protected String defaultToStringContent() {
		String sep = "";
		String result = "[";
		for (Tree<V> child : this.children) {
			result = result + sep + child.defaultToStringContent();
			sep = "|";
		}
		return result + "]";
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 11 * hash + this.children.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Node<?> other = (Node<?>) obj;
		return this.children.equals(other.children);
	}

}
