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
package ch.bfh.unicrypt.helper.array.interfaces;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;

/**
 * This interface extends {@link DefaultValueArray} with some additional methods to perform binary operations on
 * existing binary arrays. For this to work, the generic type of the array needs to be interpretable as binary data. The
 * generic types for which this interface is intended to be applied are {@link Boolean}, {@link Byte}, and {@link Long}.
 * <p>
 * @see BitArray
 * @see ByteArray
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values in the binary array
 */
public interface BinaryArray<V>
	   extends DefaultValueArray<V> {

	/**
	 * Performs bit-wise logical AND on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the shorter one. The
	 * remaining values of the longer array are ignored.
	 * <p>
	 * @param other The second binary array
	 * @return The result from applying bit-wise logical AND to both input arrays
	 * @see BinaryArray#and(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray, boolean)
	 */
	public BinaryArray<V> and(BinaryArray<V> other);

	/**
	 * Performs bit-wise logical OR on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the shorter one. The
	 * remaining values of the longer array are ignored.
	 * <p>
	 * @param other The second binary array
	 * @return The result from applying bit-wise logical OR to both input arrays
	 * @see BinaryArray#or(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray, boolean)
	 */
	public BinaryArray<V> or(BinaryArray<V> other);

	/**
	 * Performs bit-wise logical XOR on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the shorter one. The
	 * remaining values of the longer array are ignored.
	 * <p>
	 * @param other The second binary array
	 * @return The result from applying bit-wise logical XOR to both input arrays
	 * @see BinaryArray#xor(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray, boolean)
	 */
	public BinaryArray<V> xor(BinaryArray<V> other);

	/**
	 * Performs bit-wise logical AND on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the longer one. To
	 * perform the operation, the shorter array is extended with 0 or 1 bits, depending on the actual value of the
	 * parameter {@code fillBit}.
	 * <p>
	 * @param other   The second binary array
	 * @param fillBit The value of the bits extending the shorter array
	 * @return The result from applying bit-wise logical AND to both input arrays
	 * @see BinaryArray#and(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray)
	 */
	public BinaryArray<V> and(BinaryArray<V> other, boolean fillBit);

	/**
	 * Performs bit-wise logical OR on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the longer one. To
	 * perform the operation, the shorter array is extended with 0 or 1 bits, depending on the actual value of the
	 * parameter {@code fillBit}.
	 * <p>
	 * @param other   The second binary array
	 * @param fillBit The value of the bits extending the shorter array
	 * @return The result from applying bit-wise logical OR to both input arrays
	 * @see BinaryArray#or(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray)
	 */
	public BinaryArray<V> or(BinaryArray<V> other, boolean fillBit);

	/**
	 * Performs bit-wise logical XOR on the binary representations of the values stored in two binary arrays. If the
	 * given arrays are different in length, the resulting binary array inherits the length of the longer one. To
	 * perform the operation, the shorter array is extended with 0 or 1 bits, depending on the actual value of the
	 * parameter {@code fillBit}.
	 * <p>
	 * @param other   The second binary array
	 * @param fillBit The value of the bits extending the shorter array
	 * @return The result from applying bit-wise logical XOR to both input arrays
	 * @see BinaryArray#xor(ch.bfh.unicrypt.helper.array.interfaces.BinaryArray)
	 */
	public BinaryArray<V> xor(BinaryArray<V> other, boolean fillBit);

	/**
	 * Performs bit-wise logical NOT on the binary representation of the values stored in the input array.
	 * <p>
	 * @return The result from applying bit-wise logical NOT to the input array
	 */
	public BinaryArray<V> not();

	public Class<?> getBaseClass();

}
