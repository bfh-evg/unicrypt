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
package ch.bfh.unicrypt.helper.array.classes;

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.array.abstracts.AbstractBinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.BinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayFinalToString;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class is provides an implementation for immutable arrays of type {@code byte}/{@code Byte}. Internally, the byte
 * values are stored in an ordinary Java byte array. To inherit the methods from the interface {@link BinaryArray}, it
 * uses the binary representation of the {@code byte} values stored in the array. This class serves as an efficient base
 * class for the implementations of {@link BooleanArray}.
 * <p>
 * @see BooleanArray
 * @author Rolf Haenni
 */
public class ByteArrayFinal
	   extends AbstractBinaryArray<ByteArrayFinal, Byte> {

	// two static variables for the two borderline byte values
	private static final byte ALL_ZERO = (byte) 0x00;
	private static final byte ALL_ONE = (byte) 0xFF;

	// the internal Java array containing the byte values
	private byte[] bytes;

	// a flag that indicates whether the bits of each byte value have been reversed
	private boolean byteReversed;

	// a flag indicating whether the internal byte array is identical to the external
	private boolean normalized;

	protected ByteArrayFinal(byte fillByte, int arrayLength) {
		this(new byte[]{fillByte}, arrayLength);
	}

	protected ByteArrayFinal(byte[] bytes) {
		this(bytes, bytes.length);
	}

	protected ByteArrayFinal(byte[] bytes, int length) {
		this(bytes, length, 0, false, 0, 0, length, false);
	}

	protected ByteArrayFinal(byte[] bytes, int length, int rangeOffset, boolean reverse, int trailer, int header, int rangeLength, boolean byteReversed) {
		super(ByteArrayFinal.class, length, rangeOffset, reverse, ALL_ZERO, trailer, header, rangeLength);
		this.bytes = bytes;
		this.byteReversed = byteReversed;
		if (bytes.length == 1 && length == rangeLength) {
			this.uniform = true;
		}
		this.normalized = (trailer == 0) && (header == 0) && (rangeOffset == 0) && !reverse && byteReversed;
	}

	/**
	 * Creates a new {@code ByteArray} instance of a given length. Depending on the parameter {@code fillBit}, all its
	 * bytes are identical to either {@code ALL_ZERO} or {@code ALL_ONE}. This method is a special case of
	 * {@link ByteArray#getInstance(byte, int)}.
	 * <p>
	 * @param fillBit A flag indicating whether the values in the new array are {@code ALL_ZERO} or {@code ALL_ONE}
	 * @param length  The length of the new array
	 * @return The new array
	 */
	public static ByteArrayFinal getInstance(boolean fillBit, int length) {
		if (fillBit) {
			return ByteArrayFinal.getInstance(ALL_ONE, length);
		} else {
			return ByteArrayFinal.getInstance(ALL_ZERO, length);
		}
	}

	/**
	 * Creates a new {@code ByteArray} instance of a given length. All its values are identical to the given byte.
	 * <p>
	 * @param fillByte The byte included in the new array
	 * @param length   The length of the new array
	 * @return The new array
	 */
	public static ByteArrayFinal getInstance(byte fillByte, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayFinal(fillByte, length);
	}

	/**
	 * Creates a new {@code ByteArray} instance from a given Java array of {@code byte} values. The Java array is copied
	 * for internal storage. The length and the indices of the values of the resulting array correspond to the given
	 * Java array.
	 * <p>
	 * @param bytes The Java array of {@code byte} values
	 * @return The new array
	 */
	public static ByteArrayFinal getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		byte[] result = new byte[bytes.length];
		int i = 0;
		for (byte value : bytes) {
			result[i++] = value;
		}
		return new ByteArrayFinal(result);
	}

	/**
	 * Transforms a given immutable array of type {@code Byte} into a {@code ByteArray} instance. If the given immutable
	 * array is already an instance of {@code ByteArray}, it is returned without doing anything. Otherwise, the
	 * immutable array is transformed into a Java array for internal storage.
	 * <p>
	 * @param immutableArray The given immutable array
	 * @return The new array
	 */
	public static ByteArrayFinal getInstance(ImmutableArray<Byte> immutableArray) {
		if (immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof ByteArrayFinal) {
			return (ByteArrayFinal) immutableArray;
		}
		byte[] result = new byte[immutableArray.getLength()];
		int i = 0;
		for (byte value : immutableArray) {
			result[i++] = value;
		}
		return new ByteArrayFinal(result);
	}

	/**
	 * Creates a new {@code ByteArray} instance from a given Java collection of {@code Byte} values. The collection is
	 * transformed into a Java array for internal storage. The length and the indices of the values of the resulting
	 * array correspond to the given Java collection.
	 * <p>
	 * @param collection The Java collection of {@code Byte} values
	 * @return The new array
	 */
	public static ByteArrayFinal getInstance(Collection<Byte> collection) {
		if (collection == null) {
			throw new IllegalArgumentException();
		}
		byte[] result = new byte[collection.size()];
		int i = 0;
		for (Byte value : collection) {
			if (value == null) {
				throw new IllegalArgumentException();
			}
			result[i++] = value;
		}
		return new ByteArrayFinal(result);
	}

// TODO!!!
//	public static ByteArrayFinal getRandomInstance(int length) {
//		return ByteArray.getRandomInstance(length, HybridRandomByteSequence.getInstance());
//	}
//
//	public static ByteArrayFinal getRandomInstance(int length, RandomByteSequence randomByteSequence) {
//		if (length < 0 || randomByteSequence == null) {
//			throw new IllegalArgumentException();
//		}
//		return randomByteSequence.getNextByteArray(length);
//	}
// 	public static ByteArray getInstance(ByteArray... byteArrays) {
//
	public ByteArrayFinal getHashValue() {
		return this.getHashValue(HashAlgorithm.getInstance());
	}

	public ByteArrayFinal getHashValue(HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		byte[] hash = hashAlgorithm.getHashValue(this.getBytes());
		return new ByteArrayFinal(hash);
	}

	/**
	 * Creates a new {@code ByteArray} instance by reversing the bits of each byte. The order of the values in the array
	 * remains unchanged. Keeping the order of the bytes in the array distinguishes this method from
	 * {@link ByteArray#reverse()}.
	 * <p>
	 * @return The new array with the bits in each value reversed
	 */
	public ByteArrayFinal byteReverse() {
		return new ByteArrayFinal(this.bytes, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength, !this.byteReversed);
	}

	/**
	 * Transforms the {@code ByteArray} instance into a Java {@code byte} array.
	 * <p>
	 * @return The resulting Java {@code byte} array
	 */
	public byte[] getBytes() {
		// normalize for better performance
		if (!this.normalized) {
			this.normalize();
		}
		return Arrays.copyOf(this.bytes, this.length);
	}

	// copy of getAt with byte as return type
	public byte getByteAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.abstractGetByteAt(index);
	}

	// copy of abstractGetAt with byte as return type
	private byte abstractGetByteAt(int index) {
		if (index < this.trailer || index >= this.length - this.header) {
			return this.defaultValue;
		}
		int rangeIndex = index - this.trailer;
		if (this.reverse) {
			rangeIndex = this.rangeLength - rangeIndex - 1;
		}
		return this.abstractGetValueAt(this.rangeOffset + rangeIndex);
	}

	// copy of abstractGetValueAt with byte as return type
	private byte abstractGetByteValueAt(int index) {
		byte result = this.bytes[index % this.bytes.length];
		if (this.byteReversed) {
			result = MathUtil.reverse(result);
		}
		return result;
	}

	private void normalize() {
		if (!this.normalized) {
			byte[] newBytes = new byte[this.length];
			for (int i = 0; i < this.length; i++) {
				newBytes[i] = this.abstractGetByteAt(i);
			}
			this.bytes = newBytes;
			this.header = 0;
			this.trailer = 0;
			this.rangeOffset = 0;
			this.rangeLength = this.length;
			this.byteReversed = false;
			this.reverse = false;
			this.normalized = true;
		}
	}

	@Override
	protected String defaultToStringValue() {
		String str = ByteArrayFinalToString.getInstance(ByteArrayFinalToString.Radix.HEX, "|").convert(this);
		return "\"" + str + "\"";
	}

	@Override
	protected Byte abstractGetValueAt(int index) {
		byte result = this.bytes[index % this.bytes.length];
		if (this.byteReversed) {
			result = MathUtil.reverse(result);
		}
		return result;
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArrayFinal abstractAppend(ImmutableArray<Byte> other) {

		// normalize byte arrays for better performance
		this.normalize();

		// create new byte array and copy the bytes of the first one as prefix
		byte[] result = Arrays.copyOf(this.bytes, this.length + other.getLength());

		// check if other is an instance from this class
		if (this.getClass().isInstance(other)) {
			// if yes, cast and normalize it for better performance
			ByteArrayFinal otherByteArray = (ByteArrayFinal) other;
			if (!otherByteArray.normalized) {
				otherByteArray.normalize();
			}
			// copy the bytes of as suffix
			System.arraycopy(otherByteArray.bytes, 0, result, this.length, otherByteArray.length);
		} else {
			// copy the bytes one by one (this is the slowest execution case)
			for (int i = 0; i < other.getLength(); i++) {
				result[this.length + i] = other.getAt(i);
			}

		}
		return new ByteArrayFinal(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArrayFinal abstractInsertAt(int index, Byte newByte) {

		// normalize byte arrays for better performance
		this.normalize();

		// create new byte array and copy the bytes
		byte[] result = new byte[this.length + 1];
		System.arraycopy(this.bytes, 0, result, 0, index);
		System.arraycopy(this.bytes, index, result, index + 1, this.length - index);

		// copy the inserted byte
		result[index] = newByte;

		// generate and return result
		return new ByteArrayFinal(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArrayFinal abstractReplaceAt(int index, Byte newByte) {

		// normalize byte arrays for better performance
		this.normalize();

		// create copy of the bytes
		byte[] result = Arrays.copyOf(this.bytes, this.length);

		// copy the inserted byte
		result[index] = newByte;

		// generate and return result
		return new ByteArrayFinal(result);
	}

	@Override
	protected ByteArrayFinal abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new ByteArrayFinal(this.bytes, length, rangeOffset, this.reverse, trailer, header, rangeLength, this.byteReversed);
	}

	@Override
	protected ByteArrayFinal abstractReverse() {
		// switch trailer and header
		return new ByteArrayFinal(this.bytes, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength, this.byteReversed);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArrayFinal abstractNot() {
		// normalize byte arrays for better performance
		this.normalize();

		// create resulting byte array
		byte[] result = new byte[this.length];

		// perform operation
		for (int i = 0; i < this.length; i++) {
			result[i] = MathUtil.not(this.bytes[i]);
		}
		return new ByteArrayFinal(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArrayFinal abstractAndOrXor(Operator operator, ByteArrayFinal other, boolean maxLength, boolean fillBit) {

		// normalize byte arrays for better performance
		this.normalize();
		other.normalize();

		// determine minimal and maximal length
		int min = Math.min(this.length, other.length);
		int max = Math.max(this.length, other.length);

		// create resulting byte array
		byte[] result = new byte[maxLength ? max : min];

		// perform operation between both byte arrays
		switch (operator) {
			case AND:
				for (int i = 0; i < min; i++) {
					result[i] = MathUtil.and(this.bytes[i], other.bytes[i]);
				}
				break;
			case OR:
				for (int i = 0; i < min; i++) {
					result[i] = MathUtil.or(this.bytes[i], other.bytes[i]);
				}
				break;
			case XOR:
				for (int i = 0; i < min; i++) {
					result[i] = MathUtil.xor(this.bytes[i], other.bytes[i]);
				}
				break;
		}

		// if necessary fill up new byte array
		if (maxLength) {
			byte[] longer = (this.length == max) ? this.bytes : other.bytes;
			switch (operator) {
				case AND:
					if (fillBit) { // copy bytes from longer byte array
						System.arraycopy(longer, min, result, min, max - min);
					}
					break;
				case OR:
					if (fillBit) { // fill up with 1's
						for (int i = min; i < max; i++) {
							result[i] = ALL_ONE;
						}
					} else { // copy bytes from longer byte array
						System.arraycopy(longer, min, result, min, max - min);
					}
					break;
				case XOR:
					if (fillBit) { // copy negated bytes from longer byte array
						for (int i = min; i < max; i++) {
							result[i] = MathUtil.not(longer[i]);
						}
					} else { // copy bytes from longer byte array
						System.arraycopy(longer, min, result, min, max - min);
					}
					break;
			}
		}
		return new ByteArrayFinal(result);
	}

// adds an overall, possibly negative shift
	protected byte getByteAt(int shift, int index, int rangeOffset, int rangeLength) {
		// Java8: use floorMod and floorDiv to handle negative values properly
		int shiftMod = MathUtil.modulo(shift, Byte.SIZE);
		index = index + MathUtil.divide(shift, Byte.SIZE);
		byte byte1 = this.getByteAt(index, rangeOffset, rangeLength);
		if (shiftMod == 0) {
			return byte1;
		}
		byte byte2 = this.getByteAt(index + 1, rangeOffset, rangeLength);
		return MathUtil.or(MathUtil.shiftRight(byte1, shiftMod),
						   MathUtil.shiftLeft(byte2, Byte.SIZE - shiftMod));
	}

	// makes the byte array arbitraily long and filters out unused bits
	private byte getByteAt(int index, int rangeOffset, int rangeLength) {
		if (index < 0 || index >= this.length || rangeLength == 0) {
			return ALL_ZERO;
		}
		byte mask = this.getMaskAt(index, rangeOffset, rangeLength);
		return MathUtil.and(this.abstractGetByteAt(index), mask);
	}

	// computes the corresponding mask
	private byte getMaskAt(int leftIndex, int leftOffset, int rangeLength) {
		int rightIndex = this.length - leftIndex - 1;
		int rightOffset = this.length * Byte.SIZE - leftOffset - rangeLength;

		int leftDiv = MathUtil.divide(leftOffset, Byte.SIZE);
		int rightDiv = MathUtil.divide(rightOffset, Byte.SIZE);

		if (leftIndex < leftDiv || rightIndex < rightDiv) {
			return ALL_ZERO;
		}
		if (leftIndex > leftDiv && rightIndex > rightDiv) {
			return ALL_ONE;
		}
		byte mask1 = (leftIndex == leftDiv) ? MathUtil.shiftLeft(ALL_ONE, MathUtil.modulo(leftOffset, Byte.SIZE)) : ALL_ONE;
		byte mask2 = (rightIndex == rightDiv) ? MathUtil.shiftRight(ALL_ONE, MathUtil.modulo(rightOffset, Byte.SIZE)) : ALL_ONE;
		return MathUtil.and(mask1, mask2);
	}

	public static void main(String[] args) {
		byte[] ba1 = new byte[256];
		byte[] ba2 = new byte[256];
		ByteArrayFinal ban1 = ByteArrayFinal.getInstance(ba1);
		ByteArrayFinal ban2 = ByteArrayFinal.getInstance(ba2);
		ByteArray bao1 = ByteArray.getInstance(ba1);
		ByteArray bao2 = ByteArray.getInstance(ba2);

		int runs = 1000000;

		long t1 = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			byte[] ba3 = new byte[256];
			for (int j = 0; j < 256; j++) {
				ba3[j] = (byte) (ba1[j] | ba2[j]);
			}
		}

		long t2 = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			ByteArrayFinal ban3 = ban1.or(ban2);
		}

		long t3 = System.nanoTime();
		for (int i = 0; i < runs; i++) {
			ByteArray bao3 = bao1.or(bao2);
		}

		long t4 = System.nanoTime();

		System.out.println(t2 - t1);
		System.out.println(t3 - t2);
		System.out.println(t4 - t3);
	}

}
