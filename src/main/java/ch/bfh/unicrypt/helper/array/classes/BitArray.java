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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.array.abstracts.AbstractBinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.BitArrayToString;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;

/**
 * This class is provides an implementation for immutable arrays of type {@code boolean}/{@code Boolean}. For maximal
 * performance of binary operations, the boolean values are internally stored in a {@link ByteArray} instance.
 * <p>
 * @see ByteArray
 * @author R. Haenni
 * @version 2.0
 */
public class BitArray
	   extends AbstractBinaryArray<BitArray, Boolean> {

	// a static varible containing a converter to convert bit arrays to string and back
	private static final BitArrayToString STRING_CONVERTER = BitArrayToString.getInstance();
	private static final long serialVersionUID = 1L;

	// the internal ByteArray instance containing the boolean values
	private final ByteArray byteArray;

	private BitArray(ByteArray byteArray, int length) {
		this(byteArray, length, 0);
	}

	private BitArray(ByteArray byteArray, int length, int rangeOffset) {
		this(byteArray, length, rangeOffset, 0, 0, length);
	}

	private BitArray(ByteArray byteArray, int length, int rangeOffset, int trailer, int header, int rangeLength) {
		// reverse is not used (always false), default value is false
		super(BitArray.class, length, rangeOffset, false, false, trailer, header, rangeLength);
		this.byteArray = byteArray;
	}

	/**
	 * Creates a new bit array of a given length with values identical to {@code fillBit}.
	 * <p>
	 * @param fillBit The boolean value contained in the new bit array
	 * @param length  The length of the new bit array
	 * @return The new bit array
	 */
	public static BitArray getInstance(boolean fillBit, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		int byteLength = MathUtil.divideUp(length, Byte.SIZE);
		return new BitArray(ByteArray.getInstance(fillBit, byteLength), length);
	}

	/**
	 * Creates a new bit array from a given {@code ByteArray} instance. Its length and boolean values correspond to the
	 * bits in the binary representation of the given byte array. This method is a special case of
	 * {@link BitArray#getInstance(ch.bfh.unicrypt.helper.array.classes.ByteArray, int)} with the length computed
	 * automatically.
	 * <p>
	 * @param byteArray The given byte array
	 * @return The new bit array
	 */
	public static BitArray getInstance(ByteArray byteArray) {
		if (byteArray == null) {
			throw new IllegalArgumentException();
		}
		return new BitArray(byteArray, byteArray.getLength() * Byte.SIZE);
	}

	/**
	 * Creates a new bit array of a given length from a given byte array. Its boolean values correspond to the bits in
	 * the binary representation of the of the given byte array. Extra bits exceeding the given length are ignored.
	 * <p>
	 * @param byteArray The given byte array
	 * @param length    The length of the new bit array
	 * @return The new bit array
	 */
	public static BitArray getInstance(ByteArray byteArray, int length) {
		if (byteArray == null || length < 0 || length > byteArray.getLength() * Byte.SIZE) {
			throw new IllegalArgumentException();
		}
		return new BitArray(byteArray, length);
	}

	/**
	 * Creates a new bit array from a given Java array of {@code boolean} values. The Java array is copied for internal
	 * storage. The length and the indices of the values of the resulting array correspond to the given Java array.
	 * <p>
	 * @param bits The Java array of {@code boolean} values
	 * @return The new bit array
	 */
	public static BitArray getInstance(boolean... bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		int bitLength = bits.length;
		int byteLength = MathUtil.divideUp(bitLength, Byte.SIZE);
		byte[] bytes = new byte[byteLength];
		for (int i = 0; i < bitLength; i++) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			if (bits[i]) {
				bytes[byteIndex] = MathUtil.setBit(bytes[byteIndex], bitIndex);
			}
		}
		return new BitArray(ByteArray.getInstance(bytes), bitLength);
	}

	/**
	 * Creates a new bit array from a given sequence of bits. The sequence is transformed into a Java byte array for
	 * internal storage. Null values are eliminated.
	 * <p>
	 * @param bits The sequence of bits
	 * @return The new bit array
	 */
	public static BitArray getInstance(Sequence<Boolean> bits) {
		if (bits == null || bits.isInfinite()) {
			throw new IllegalArgumentException();
		}
		bits = bits.filter(Sequence.NOT_NULL);
		int bitLength = bits.getLength().intValue();
		int byteLength = MathUtil.divideUp(bitLength, Byte.SIZE);
		byte[] bytes = new byte[byteLength];
		int i = 0;
		for (Boolean bit : bits) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			if (bit) {
				bytes[byteIndex] = MathUtil.setBit(bytes[byteIndex], bitIndex);
			}
			i++;
		}
		return new BitArray(ByteArray.getInstance(bytes), bitLength);
	}

	/**
	 * Creates a new bit array from a given bit string using the string converter defined for this class. For example,
	 * "0111000011" creates a bit array of length 10 with bit 0 (false) at index 0, 1 (true) at index 1, 1 (true) at
	 * index 2, etc. The same string converter is used by {@link BitArray#toString()}.
	 * <p>
	 * @param bitString The bit string
	 * @return The new bit array
	 */
	public static BitArray getInstance(String bitString) {
		if (bitString == null) {
			throw new IllegalArgumentException();
		}
		return STRING_CONVERTER.reconvert(bitString);
	}

	/**
	 * Creates a new random bit array of a given length. It uses the library's standard randomness source to create the
	 * random bits.
	 * <p>
	 * @param length The length of the random bit array
	 * @return The new random bit array
	 */
	public static BitArray getRandomInstance(int length) {
		return BitArray.getRandomInstance(length, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Creates a new random bit array of a given length. It uses a given instance of {@code RandomByteSequence} instance
	 * as randomness source to create the random bits.
	 * <p>
	 * @param length             The length of the random bit array
	 * @param randomByteSequence The randomness source
	 * @return The new random bit array
	 */
	public static BitArray getRandomInstance(int length, RandomByteSequence randomByteSequence) {
		if (length < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		int byteLength = MathUtil.divideUp(length, Byte.SIZE);
		return new BitArray(randomByteSequence.group(byteLength).get(), length);
	}

	/**
	 * Transforms the bit array into a Java array of {@code boolean} values.
	 * <p>
	 * @return The resulting Java array
	 */
	public boolean[] getBits() {
		boolean[] result = new boolean[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	/**
	 * Transforms the bit array into a byte array. If the length of the bit array is not a multiple of 8, the
	 * corresponding number of extra 0 bits is added. This implies that the resulting byte array can not always be
	 * transformed back into the same bit array.
	 * <p>
	 * @return The resulting byte array
	 */
	public ByteArray getByteArray() {
		int length = MathUtil.divideUp(this.length, Byte.SIZE);
		byte[] bytes = new byte[length];
		for (int index = 0; index < length; index++) {
			bytes[index] = this.byteArray.getByteAt(this.rangeOffset - this.trailer, index, this.rangeOffset,
													this.rangeLength);
		}
		return ByteArray.getInstance(bytes);
	}

	@Override
	protected String defaultToStringContent() {
		String str = STRING_CONVERTER.convert(this);
		return "\"" + str + "\"";
	}

	@Override
	protected Boolean abstractGetValueAt(int index) {
		int byteIndex = index / Byte.SIZE;
		int bitIndex = index % Byte.SIZE;
		return MathUtil.getBit(this.byteArray.getAt(byteIndex), bitIndex);
	}

	@Override
	protected BitArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new BitArray(this.byteArray, length, rangeOffset, trailer, header, rangeLength);
	}

	@Override
	protected BitArray abstractInsertAt(int index, Boolean value) {
		BitArray bitArray1 = this.extractPrefix(index);
		BitArray bitArray2 = this.extract(index, this.length - index);
		if (value) {
			bitArray2 = BitArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BitArray abstractReplaceAt(int index, Boolean value) {
		BitArray bitArray1 = this.extractPrefix(index);
		BitArray bitArray2 = this.extract(index + 1, this.length - index - 1);
		if (value) {
			bitArray2 = BitArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BitArray abstractAppend(ImmutableArray<Boolean> other) {
		return this.abstractAppend(BitArray.getInstance(other.getSequence()));
	}

	protected BitArray abstractAppend(BitArray other) {
		// make this BitArray right-aligned with a ByteArray
		int shift = (Byte.SIZE - this.length % Byte.SIZE) % Byte.SIZE;
		ByteArray byteArray1 = this.addPrefix(shift).getByteArray();
		ByteArray byteArray2 = other.getByteArray();
		return new BitArray(byteArray1.append(byteArray2), this.length + other.length, shift);
	}

	@Override
	protected BitArray abstractReverse() {
		// reverse byteArrary, adjust offset, switch trailer and header (reverse is always false)
		int newOffset = this.byteArray.getLength() * Byte.SIZE - this.rangeOffset - this.rangeLength;
		return new BitArray(this.byteArray.bitReverse(), this.length, newOffset, this.header, this.trailer,
							this.rangeLength);
	}

	@Override
	protected BitArray abstractAndOrXor(Operator operator, BitArray other, boolean maxLength, boolean fillBit) {
		// make the shorter BitArray right-aligned with a ByteArray
		int minLength = Math.min(this.length, other.length);
		int shift = (Byte.SIZE - minLength % Byte.SIZE) % Byte.SIZE;
		ByteArray byteArray1 = this.addPrefix(shift).getByteArray();
		ByteArray byteArray2 = other.addPrefix(shift).getByteArray();
		ByteArray result = byteArray1.abstractAndOrXor(operator, byteArray2, maxLength, fillBit);
		int newLength = maxLength ? Math.max(this.length, other.length) : minLength;
		return new BitArray(result, newLength, shift);
	}

	@Override
	protected BitArray abstractNot() {
		return new BitArray(this.getByteArray().not(), this.length);
	}

	@Override
	public Class<?> getBaseClass() {
		return BitArray.class;
	}

}
