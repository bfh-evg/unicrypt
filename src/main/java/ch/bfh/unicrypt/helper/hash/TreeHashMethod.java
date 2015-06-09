/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.ByteArrayConverter;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;
import java.nio.ByteBuffer;

/**
 *
 * @author rolfhaenni
 * @param <V>
 */
public class TreeHashMethod<V>
	   extends HashMethod<V> {
	private static final long serialVersionUID = 1L;

	protected Aggregator<V> aggregator;
	protected boolean recursive;

	protected TreeHashMethod(HashAlgorithm hashAlgorithm, ByteArrayConverter<V> converter) {
		super(hashAlgorithm, converter);
		this.recursive = true;
	}

	protected TreeHashMethod(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		super(hashAlgorithm, converter);
		this.aggregator = aggregator;
		this.recursive = false;
	}

	public static <V> TreeHashMethod<V> getInstance(ByteArrayConverter<V> converter) {
		return TreeHashMethod.getInstance(HashAlgorithm.getInstance(), converter);
	}

	public static <V> TreeHashMethod<V> getInstance(HashAlgorithm hashAlgorithm, ByteArrayConverter<V> converter) {
		if (hashAlgorithm == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new TreeHashMethod(hashAlgorithm, converter);
	}

	public static <V> TreeHashMethod<V> getInstance(Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		return TreeHashMethod.getInstance(HashAlgorithm.getInstance(), aggregator, converter);
	}

	public static <V> TreeHashMethod<V>
	   getInstance(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		if (hashAlgorithm == null || aggregator == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new TreeHashMethod(hashAlgorithm, aggregator, converter);
	}

	public ByteArray getHashValue(Tree<V> tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		if (this.recursive) {
			return new SafeByteArray(this.getRecursiveHashValue(tree));
		}
		V value = this.aggregator.aggregate(tree);
		ByteArray byteArray = this.converter.convert(value);
		return this.hashAlgorithm.getHashValue(byteArray);

	}

	private byte[] getRecursiveHashValue(Tree<V> tree) {

		// Case 1: tree is a leaf
		if (tree.isLeaf()) {
			return this.getHashValue(((Leaf<V>) tree).getValue()).getBytes();
		}

		// Case 2: tree is a node
		Node<V> node = (Node<V>) tree;
		ByteBuffer bb = ByteBuffer.allocate(node.getSize() * this.hashAlgorithm.getByteLength());
		for (Tree<V> child : node.getChildren()) {
			bb.put(this.getRecursiveHashValue(child));
		}
		return this.hashAlgorithm.getHashValue(bb.array());
	}

	public Aggregator<V> getAggregator() {
		return this.aggregator;
	}

	public boolean isRecursive() {
		return this.recursive;
	}

}
