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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.UniCrypt;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.ByteArrayConverter;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;
import java.nio.ByteBuffer;

/**
 * The purpose of this class is to extend the applicability of hash algorithms from single byte array input objects to
 * instances of the class {@link Tree}{@code <V>}. There are multiple methods of computing the hash value of a tree.
 * <p>
 * The fist method is to aggregate the values stored in the tree into a single value of type {@code V}, convert this
 * value into a byte array, and finally compute the hash value using a hash algorithm. For this, an aggregator of type
 * {@code Aggregator<V>} and a converter of type {@code ByteArrayConverter<V>} are needed.
 * <p>
 * The second method first converts each value stored in the tree into a byte array and then computes the hash value
 * recursively,or example {@code h(h(b1)|h(h(b2)|h(b3))|h(b4))} for input byte arrays {@code (b1,(b2,b3),b4)}.
 * <p>
 * All instances of this class allow the computation of hash values for single values of type {@code V} and trees of
 * type {@code Tree<V>}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of this hash method
 * @see HashAlgorithm
 * @see Aggregator
 * @see ByteArrayConverter
 */
public class HashMethod<V>
	   extends UniCrypt {

	// the hash algorithm applied to the byte arrays
	protected final HashAlgorithm hashAlgorithm;

	// the converter used to convert values of the generic type V into byte arrays
	protected final ByteArrayConverter<V> converter;

	// the aggregator used in the non-recursive hash value computation
	protected final Aggregator<V> aggregator;

	// a flag indicating the mode of computation
	protected final boolean recursive;

	// Case 1: recursive hashing
	protected HashMethod(HashAlgorithm hashAlgorithm, ByteArrayConverter<V> converter) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.aggregator = null; // no aggregator is needed for recursive hashing
		this.recursive = true;
	}

	// Case 2: hashing after aggregation
	protected HashMethod(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.aggregator = aggregator;
		this.recursive = false;
	}

	/**
	 * Returns a new hash method which computes the hash values recursively using the default hash algorithm. The
	 * ByteArray converter is given as parameter.
	 * <p>
	 * @param <V>       The generic type of the new hash method
	 * @param converter The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(ByteArrayConverter<V> converter) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), converter);
	}

	/**
	 * Returns a new hash method which computes the hash values recursively. Both the hash algorithm and the ByteArray
	 * converter are given as parameters.
	 * <p>
	 * @param <V>           The generic type of the new hash method
	 * @param hashAlgorithm The given hash algorithm
	 * @param converter     The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(HashAlgorithm hashAlgorithm, ByteArrayConverter<V> converter) {
		if (hashAlgorithm == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, converter);
	}

	/**
	 * Returns a new hash method which computes the hash values by first applying an aggregator to the given tree. The
	 * aggregator and the ByteArray converter are given as parameters. The resulting hash method uses the default hash
	 * algorithm.
	 * <p>
	 * @param <V>        The generic type of the new hash method
	 * @param aggregator The given aggregator
	 * @param converter  The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), aggregator, converter);
	}

	/**
	 * Returns a new hash method which computes the hash values by first applying an aggregator to the given tree. The
	 * hash algorithm, the aggregator, and the ByteArray converter are given as parameters.
	 * <p>
	 * @param <V>           The generic type of the new hash method
	 * @param hashAlgorithm The given hash algorithm
	 * @param aggregator    The given aggregator
	 * @param converter     The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator, ByteArrayConverter<V> converter) {
		if (hashAlgorithm == null || aggregator == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, aggregator, converter);
	}

	/**
	 * Returns the hash value of a single value.
	 * <p>
	 * @param value The given value
	 * @return The resulting hash value
	 */
	public ByteArray getHashValue(V value) {
		if (value == null) {
			throw new IllegalArgumentException();
		}
		ByteArray byteArray = this.converter.convert(value);
		return this.hashAlgorithm.getHashValue(byteArray);
	}

	/**
	 * Returns the hash value of a tree of values.
	 * <p>
	 * @param tree The given tree
	 * @return The resulting hash value
	 */
	public ByteArray getHashValue(Tree<V> tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		// Case 1: recursive hashing
		if (this.recursive) {
			return new SafeByteArray(this.getRecursiveHashValue(tree));
		}
		// Case 2: first aggregate the tree, convert the result to ByteArray, and compute hash
		V value = this.aggregator.aggregate(tree);
		ByteArray byteArray = this.converter.convert(value);
		return this.hashAlgorithm.getHashValue(byteArray);

	}

	/**
	 * Returns that hash algorithm used in this hash method.
	 * <p>
	 * @return The hash algorithm
	 */
	public HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	/**
	 * Returns that ByteArray converter used in this hash method.
	 * <p>
	 * @return The ByteArray converter
	 */
	public ByteArrayConverter<V> getConverter() {
		return this.converter;
	}

	/**
	 * Returns the aggregator used in this hash method, or {@literal null} if hash values are computed recursively.
	 * <p>
	 * @return The aggregator
	 */
	public Aggregator<V> getAggregator() {
		return this.aggregator;
	}

	/**
	 * Checks if the hash values are computed recursively.
	 * <p>
	 * @return {@literal true} if hash values are computed recursively, {@literal false} otherwise
	 */
	public boolean isRecursive() {
		return this.recursive;
	}

	// a private method to compute hash value recursively
	private byte[] getRecursiveHashValue(Tree<V> tree) {
		// Case 1: tree is a leaf
		if (tree.isLeaf()) {
			Leaf<V> leaf = (Leaf<V>) tree;
			ByteArray byteArray = this.converter.convert(leaf.getValue());
			return this.hashAlgorithm.getHashValue(byteArray.getBytes());
		}
		// Case 2: tree is a node
		Node<V> node = (Node<V>) tree;
		ByteBuffer bb = ByteBuffer.allocate(node.getSize() * this.hashAlgorithm.getByteLength());
		for (Tree<V> child : node.getChildren()) {
			bb.put(this.getRecursiveHashValue(child));
		}
		return this.hashAlgorithm.getHashValue(bb.array());
	}

}
