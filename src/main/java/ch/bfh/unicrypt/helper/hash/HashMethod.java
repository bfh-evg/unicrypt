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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.ByteArrayToByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;
import java.nio.ByteBuffer;

/**
 * The purpose of this class is to extend the applicability of hash algorithms from single byte array input objects to
 * arbitrary instances of type {@code V} or instances of the class {@link Tree}{@code <V>}. There are multiple methods
 * of computing the hash value of a tree. We call them CRH (convert, mode hash), ACH (aggregate, convert, hash), and CAH
 * (convert, aggregate, hash).
 * <ul>
 * <li>
 * CRH: This method converts each value stored in the tree into a byte array and then computes the hash value
 * recursively,or example {@code h(h(b1)|h(h(b2)|h(b3))|h(b4))} for input byte arrays {@code (b1,(b2,b3),b4)}.
 * </li>
 * <li>
 * ACH: This method aggregates the values stored in the tree into a single value of type {@code V}, converts this value
 * into a byte array, and finally computes the hash value using a hash algorithm. For this, an aggregator of type
 * {@code Aggregator<V>} and a converter of type {@code Converter<V,ByteArray>} are needed.
 * </li>
 * <li>
 * CAH: This method converts each value stored in the tree into a byte array, aggregates the resulting tree of byte
 * arrays into a single byte array, and finally computes the hash value using a hash algorithm. For this, a converter of
 * type {@code Converter<V,ByteArray>} and an aggregator of type {@code Aggregator<ByteArray>} and a are needed.
 * </li>
 * </ul>
 * All instances of this class allow the computation of hash values for single values of type {@code V} (using the
 * converter only) and trees of type {@code Tree<V>}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of this hash method
 * @see HashAlgorithm
 * @see Aggregator
 */
public class HashMethod<V>
	   extends UniCrypt {

	private enum Mode {

		CRH, ACH, CAH

	};

	private static final long serialVersionUID = 1L;

	// the hash algorithm applied to the byte arrays
	private final HashAlgorithm hashAlgorithm;

	// the converter used to convert values of the generic type V into byte arrays
	private final Converter<V, ByteArray> converter;

	// the aggregator used in ACH
	private final Aggregator<V> valueAggregator;

	// the aggregator used in CAH
	private final Aggregator<ByteArray> byteArrayAggregator;

	// the seleced mode (CRH, ACH, CAH)
	private final Mode mode;

	// Case 1: CRH (convert, mode hash)
	private HashMethod(HashAlgorithm hashAlgorithm, Converter<V, ByteArray> converter) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.valueAggregator = null; // valueAggregator is not needed in CRH
		this.byteArrayAggregator = null; // byteArrayAggregator is not needed in CRH
		this.mode = Mode.CRH;
	}

	// Case 2: ACH (aggregate, convert, hash)
	protected HashMethod(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator, Converter<V, ByteArray> converter) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.valueAggregator = aggregator;
		this.byteArrayAggregator = null; // byteArrayAggregator is not needed in ACH
		this.mode = Mode.ACH;
	}

	// Case 3: CAH (convert, aggregate, hash)
	protected HashMethod(HashAlgorithm hashAlgorithm, Converter<V, ByteArray> converter,
		   Aggregator<ByteArray> aggregator) {
		this.hashAlgorithm = hashAlgorithm;
		this.converter = converter;
		this.valueAggregator = null; // valueAggregator is not needed in CAH
		this.byteArrayAggregator = aggregator;
		this.mode = Mode.CAH;
	}

	/**
	 * Returns a new CRH hash method of type {@code ByteArray}, using the default hash algorithm.
	 * <p>
	 * @return The new hash method
	 */
	public static HashMethod<ByteArray> getInstance() {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), ByteArrayToByteArray.getInstance());
	}

	/**
	 * Returns a new CRH hash method of type {@code ByteArray}, using a given hash algorithm.
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @return The new hash method
	 */
	public static HashMethod<ByteArray> getInstance(HashAlgorithm hashAlgorithm) {
		return HashMethod.getInstance(hashAlgorithm, ByteArrayToByteArray.getInstance());
	}

	/**
	 * Returns a new ACH/CAH hash method of type {@code ByteArray}, using the default hash algorithm and a given byte
	 * array aggregator.
	 * <p>
	 * @param aggregator The given aggregator
	 * @return The new hash method
	 */
	public static HashMethod<ByteArray> getInstance(Aggregator<ByteArray> aggregator) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), aggregator, ByteArrayToByteArray.getInstance());
	}

	/**
	 * Returns a new ACH/CAH hash method of type {@code ByteArray}, using a given hash algorithm and a given byte array
	 * aggregator
	 * <p>
	 * @param hashAlgorithm The given hash algorithm
	 * @param aggregator    The given aggregator
	 * @return The new hash method
	 */
	public static HashMethod<ByteArray> getInstance(HashAlgorithm hashAlgorithm, Aggregator<ByteArray> aggregator) {
		return HashMethod.getInstance(hashAlgorithm, aggregator, ByteArrayToByteArray.getInstance());
	}

	/**
	 * Returns a new CRH hash method using the default hash algorithm. The byte array converter is given as parameter.
	 * <p>
	 * @param <V>       The generic type of the new hash method
	 * @param converter The given byte array converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(Converter<V, ByteArray> converter) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), converter);
	}

	/**
	 * Returns a new CRH hash method. Both the hash algorithm and the byte array converter are given as parameters.
	 * <p>
	 * @param <V>           The generic type of the new hash method
	 * @param hashAlgorithm The given hash algorithm
	 * @param converter     The given byte array converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(HashAlgorithm hashAlgorithm, Converter<V, ByteArray> converter) {
		if (hashAlgorithm == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, converter);
	}

	/**
	 * Returns a new ACH hash method. The aggregator and the byte array converter are given as parameters. The resulting
	 * hash method uses the default hash algorithm.
	 * <p>
	 * @param <V>        The generic type of the new hash method
	 * @param aggregator The given aggregator
	 * @param converter  The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(Aggregator<V> aggregator, Converter<V, ByteArray> converter) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), aggregator, converter);
	}

	/**
	 * Returns a new ACH hash method. The hash algorithm, the aggregator, and the byte array converter are given as
	 * parameters.
	 * <p>
	 * @param <V>           The generic type of the new hash method
	 * @param hashAlgorithm The given hash algorithm
	 * @param aggregator    The given aggregator
	 * @param converter     The given ByteArray converter
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(HashAlgorithm hashAlgorithm, Aggregator<V> aggregator,
		   Converter<V, ByteArray> converter) {
		if (hashAlgorithm == null || aggregator == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, aggregator, converter);
	}

	/**
	 * Returns a new CAH hash method. The byte array converter and the byte array aggregator are given as parameters.
	 * The resulting hash method uses the default hash algorithm.
	 * <p>
	 * @param <V>        The generic type of the new hash method
	 * @param converter  The given byte array converter
	 * @param aggregator The given byte array aggregator
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(Converter<V, ByteArray> converter, Aggregator<ByteArray> aggregator) {
		return HashMethod.getInstance(HashAlgorithm.getInstance(), converter, aggregator);
	}

	/**
	 * Returns a new CAH hash method. The hash algorithm, the byte array converter, and the byte array aggregator are
	 * given as parameters.
	 * <p>
	 * @param <V>           The generic type of the new hash method
	 * @param hashAlgorithm The given hash algorithm
	 * @param converter     The given byte array converter
	 * @param aggregator    The given byte array aggregator
	 * @return The new hash method
	 */
	public static <V> HashMethod<V> getInstance(HashAlgorithm hashAlgorithm, Converter<V, ByteArray> converter,
		   Aggregator<ByteArray> aggregator) {
		if (hashAlgorithm == null || aggregator == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new HashMethod(hashAlgorithm, converter, aggregator);
	}

	/**
	 * Returns the hash value of a single value.
	 * <p>
	 * @param value The given value
	 * @return The resulting hash value
	 */
	public final ByteArray getHashValue(V value) {
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
	public final ByteArray getHashValue(Tree<V> tree) {
		if (tree == null) {
			throw new IllegalArgumentException();
		}
		switch (this.mode) {
			case CRH: {
				Tree<ByteArray> byteArrayTree = this.converter.convert(tree);
				return new SafeByteArray(this.getRecursiveHashValue(byteArrayTree));
			}
			case ACH: {
				V value = this.valueAggregator.aggregate(tree);
				ByteArray byteArray = this.converter.convert(value);
				return this.hashAlgorithm.getHashValue(byteArray);
			}
			case CAH: {
				Tree<ByteArray> byteArrayTree = this.converter.convert(tree);
				ByteArray byteArray = this.byteArrayAggregator.aggregate(byteArrayTree);
				return this.hashAlgorithm.getHashValue(byteArray);
			}
		}
		throw new IllegalStateException(); // impossible case
	}

	/**
	 * Returns that hash algorithm used in this hash method.
	 * <p>
	 * @return The hash algorithm
	 */
	public final HashAlgorithm getHashAlgorithm() {
		return this.hashAlgorithm;
	}

	/**
	 * Returns that ByteArray converter used in this hash method.
	 * <p>
	 * @return The ByteArray converter
	 */
	public final Converter<V, ByteArray> getByteArrayConverter() {
		return this.converter;
	}

	/**
	 * Returns the aggregator used in an a ACH hash method, or {@code null} for CRH or CAH.
	 * <p>
	 * @return The aggregator
	 */
	public Aggregator<V> getValueAggregator() {
		return this.valueAggregator;
	}

	/**
	 * Returns the aggregator used in an a CAH hash method, or {@code null} for CRH or ACH.
	 * <p>
	 * @return The aggregator
	 */
	public Aggregator<ByteArray> getByteArrayAggregator() {
		return this.byteArrayAggregator;
	}

	// a private method to compute hash value recursively
	private byte[] getRecursiveHashValue(Tree<ByteArray> tree) {
		// Case 1: tree is a leaf
		if (tree.isLeaf()) {
			Leaf<ByteArray> leaf = (Leaf<ByteArray>) tree;
			return this.hashAlgorithm.getHashValue(leaf.getValue().getBytes());
		}
		// Case 2: tree is a node
		Node<ByteArray> node = (Node<ByteArray>) tree;
		ByteBuffer bb = ByteBuffer.allocate(node.getSize() * this.hashAlgorithm.getByteLength());
		for (Tree<ByteArray> child : node.getChildren()) {
			bb.put(this.getRecursiveHashValue(child));
		}
		return this.hashAlgorithm.getHashValue(bb.array());
	}

}
