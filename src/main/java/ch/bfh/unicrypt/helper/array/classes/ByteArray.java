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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.abstracts.AbstractBinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.BinaryArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayToString;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.util.Arrays;

/**
 * This class is provides an implementation for immutable arrays of type {@code byte}/{@code Byte}. Internally, the
 * bytes are stored in an ordinary Java byte array. To implement the inherited methods from the interface
 * {@link BinaryArray}, it uses the binary representation of the byte values. This class serves as an efficient base
 * class for the implementations of {@link BitArray}.
 * <p>
 * @see BitArray
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArray
	   extends AbstractBinaryArray<ByteArray, Byte> {

	// two static variables for the two borderline byte values
	private static final byte ALL_ZERO = (byte) 0x00;
	private static final byte ALL_ONE = (byte) 0xFF;

	// a static variable for the total number of byte values
	public static final int BYTE_ORDER = 1 << Byte.SIZE;

	// a static varible containing a converter to convert byte arrays to string and back
	private static final ByteArrayToString STRING_CONVERTER
		   = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "|", true);
	private static final long serialVersionUID = 1L;

	// the internal Java array containing the byte values
	protected byte[] bytes;

	// a flag indicating whether the bits of each byte value have been reversed
	private boolean bitReversed;

	// a flag indicating whether the internal byte array is identical to the external
	private boolean normalized;

	protected ByteArray(byte[] bytes) {
		this(bytes, bytes.length);
	}

	protected ByteArray(byte[] bytes, int length) {
		this(bytes, length, 0, false, 0, 0, length, false);
	}

	protected ByteArray(byte[] bytes, int length, int rangeOffset, boolean reverse, int trailer, int header,
		   int rangeLength, boolean bitReversed) {
		super(ByteArray.class, length, rangeOffset, reverse, ALL_ZERO, trailer, header, rangeLength);
		this.bytes = bytes;
		this.bitReversed = bitReversed;
		this.normalized = rangeOffset == 0 && rangeLength == length && !reverse && !bitReversed;
	}

	/**
	 * Creates a new empty byte array of length 0;
	 * <p>
	 * @return The new empty byte array
	 */
	public static ByteArray getInstance() {
		return new ByteArray(new byte[0]);
	}

	/**
	 * Creates a new byte array of a given length. Depending on the parameter {@code fillBit}, its bytes are all
	 * identical to either {@code ALL_ZERO} or {@code ALL_ONE}. This method is a special case of
	 * {@link ByteArray#getInstance(byte, Integer)}.
	 * <p>
	 * @param fillBit A flag indicating whether the values in the new byte array are {@code ALL_ZERO} or {@code ALL_ONE}
	 * @param length  The length of the new byte array
	 * @return The new byte array
	 */
	public static ByteArray getInstance(boolean fillBit, int length) {
		if (fillBit) {
			return ByteArray.getInstance(ALL_ONE, length);
		} else {
			return ByteArray.getInstance(ALL_ZERO, length);
		}
	}

	/**
	 * Creates a new byte array of a given length. All its values are identical to the given byte.
	 * <p>
	 * @param fillByte The byte included in the new byte array
	 * @param length   The length of the new byte array
	 * @return The new byte array
	 */
	public static ByteArray getInstance(byte fillByte, Integer length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[length];
		Arrays.fill(bytes, fillByte);
		return new ByteArray(bytes, length);
	}

	/**
	 * Creates a new byte array from a given Java byte array by copying its values for internal storage.
	 * <p>
	 * @param bytes The Java byte array
	 * @return The new byte array
	 */
	public static ByteArray getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(bytes.clone());
	}

	/**
	 * Creates a new byte array from a given Java integer array by casting its values to byte and copying them for
	 * internal storage. This is a convenience method for {@link ByteArray#getInstance(byte...)}.
	 * <p>
	 * @param integers The Java integer array
	 * @return The new byte array
	 */
	public static ByteArray getInstance(int... integers) {
		if (integers == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[integers.length];
		int i = 0;
		for (int integer : integers) {
			if (integer > Byte.MAX_VALUE || integer < Byte.MIN_VALUE) {
				throw new IllegalArgumentException();
			}
			bytes[i++] = MathUtil.getByte(integer);
		}
		return new ByteArray(bytes);
	}

	/**
	 * Creates a new byte array from a given sequence of bytes. The sequence is transformed into a Java byte array for
	 * internal storage. Null values are eliminated.
	 * <p>
	 * @param bytes The sequence of bytes
	 * @return The new byte array
	 */
	public static ByteArray getInstance(Sequence<Byte> bytes) {
		if (bytes == null || bytes.isInfinite()) {
			throw new IllegalArgumentException();
		}
		bytes = bytes.filter(Sequence.NOT_NULL);
		byte[] array = new byte[bytes.getLength().intValue()];
		int i = 0;
		for (Byte b : bytes) {
			array[i++] = b;
		}
		return new ByteArray(array);
	}

	/**
	 * Creates a new byte array from a given hexadecimal string using the converter defined for this class. For example,
	 * "03|A2|29|FF|96" creates a byte array of length 5 with bytes 0x03 at index 0, 0xA2 at index 1, etc. The same
	 * string converter is used by {@link ByteArray#toString()}.
	 * <p>
	 * @param hexString The hexadecimal string
	 * @return The new byte array
	 */
	public static ByteArray getInstance(String hexString) {
		if (hexString == null) {
			throw new IllegalArgumentException();
		}
		return STRING_CONVERTER.reconvert(hexString);
	}

	/**
	 * Creates a new byte array by concatenating the bytes from multiple given byte arrays. The lenght of the new byte
	 * array corresponds to the sum of the lengths of the given byte arrays.
	 * <p>
	 * @param byteArrays The given byte arrays
	 * @return The new byte array
	 */
	public static ByteArray getInstance(ByteArray... byteArrays) {
		if (byteArrays == null) {
			throw new IllegalArgumentException();
		}
		int length = 0;
		for (ByteArray byteArray : byteArrays) {
			if (byteArray == null) {
				throw new IllegalArgumentException();
			}
			length = length + byteArray.getLength();
		}
		byte[] bytes = new byte[length];
		int index = 0;
		for (ByteArray byteArray : byteArrays) {
			System.arraycopy(byteArray.getBytes(), 0, bytes, index, byteArray.getLength());
			index = index + byteArray.getLength();
		}
		return new ByteArray(bytes);
	}

	/**
	 * Creates a new random byte array of a given length. It uses the library's standard randomness source to create the
	 * random bytes.
	 * <p>
	 * @param length The length of the random byte array
	 * @return The new random byte array
	 */
	public static ByteArray getRandomInstance(int length) {
		return ByteArray.getRandomInstance(length, HybridRandomByteSequence.getInstance());
	}

	/**
	 * Creates a new random byte array of a given length. It uses a given instance of {@code RandomByteSequence}
	 * instance as randomness source to create the random bytes.
	 * <p>
	 * @param length             The length of the random byte array
	 * @param randomByteSequence The randomness source
	 * @return The new random byte array
	 */
	public static ByteArray getRandomInstance(int length, RandomByteSequence randomByteSequence) {
		if (length < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return randomByteSequence.group(length).get();
	}

	/**
	 * Computes the byte array's hash value using the library's standard hash algorithm. The result is another byte
	 * array of a fixed length.
	 * <p>
	 * @return The hash value of the byte array
	 */
	public ByteArray getHashValue() {
		return this.getHashValue(HashAlgorithm.getInstance());
	}

	/**
	 * Computes the byte array's hash value using the the specified hash algorithm. The result is another byte array
	 * array of a fixed length.
	 * <p>
	 * @param hashAlgorithm The hash algorithm used to compute the hash value
	 * @return The hash value of the byte array
	 */
	public ByteArray getHashValue(HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		this.normalize();
		byte[] hash = hashAlgorithm.getHashValue(this.bytes);
		return new ByteArray(hash);
	}

	/**
	 * Creates a new byte array by reversing the bits of each byte. The order of the values in the array remains
	 * unchanged. Keeping the order of the bytes in the array distinguishes this method from
	 * {@link ByteArray#reverse()}.
	 * <p>
	 * @return The new array with the bits in each value reversed
	 */
	public ByteArray bitReverse() {
		return new ByteArray(this.bytes, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer,
							 this.rangeLength, !this.bitReversed);
	}

	/**
	 * Transforms the byte array into a Java byte array.
	 * <p>
	 * @return The resulting Java byte array
	 */
	public byte[] getBytes() {
		// normalize for better performance
		this.normalize();
		return Arrays.copyOf(this.bytes, this.length);
	}

	/**
	 * Converts the (unsigned) byte at some given index in the array into an {@code int} value. This is a convenience
	 * method to simplify the casting and to avoid common errors with the sign bit.
	 * <p>
	 * @param index The given index
	 * @return The correspond byte converted to {@code int}
	 */
	public int getIntAt(int index) {
		return this.getByteAt(index) & 0xFF;
	}

	/**
	 * This method is a identical to {@link ImmutableArray#getAt(int)} except for the return type ({@code byte} instead
	 * of the wrapper class {@code Byte}).
	 * <p>
	 * @param index The given index
	 * @return The corresponding byte
	 * @see ImmutableArray#getAt(int)
	 */
	public byte getByteAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
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

	// changes the byte array's internal representation by setting header, trailer, rangeOffset to 0 and
	// bitReversed and reversed to false.
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
			this.bitReversed = false;
			this.reverse = false;
			this.normalized = true;
		}
	}

	@Override
	protected String defaultToStringContent() {
		String str = STRING_CONVERTER.convert(this);
		return "\"" + str + "\"";
	}

	@Override
	protected Byte abstractGetValueAt(int index) {
		if (this.bitReversed) {
			return MathUtil.reverse(this.bytes[index]);
		}
		return this.bytes[index];
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArray abstractAppend(ImmutableArray<Byte> other) {

		// normalize byte arrays for better performance
		this.normalize();

		// create new byte array and copy the bytes of the first one as prefix
		byte[] result = Arrays.copyOf(this.bytes, this.length + other.getLength());

		// check if other is an instance from this class
		if (this.getClass().isInstance(other)) {
			// if yes, cast and normalize it for better performance
			ByteArray otherByteArray = (ByteArray) other;
			otherByteArray.normalize();
			// copy the bytes of as suffix
			System.arraycopy(otherByteArray.bytes, 0, result, this.length, otherByteArray.length);
		} else {
			// copy the bytes one by one (this is the slowest execution case)
			for (int i = 0; i < other.getLength(); i++) {
				result[this.length + i] = other.getAt(i);
			}

		}
		return new ByteArray(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArray abstractInsertAt(int index, Byte newByte) {

		// normalize byte arrays for better performance
		this.normalize();

		// create new byte array and copy the bytes
		byte[] result = new byte[this.length + 1];
		System.arraycopy(this.bytes, 0, result, 0, index);
		System.arraycopy(this.bytes, index, result, index + 1, this.length - index);

		// copy the inserted byte
		result[index] = newByte;

		// generate and return result
		return new ByteArray(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArray abstractReplaceAt(int index, Byte newByte) {

		// normalize byte arrays for better performance
		this.normalize();

		// create copy of the bytes
		byte[] result = Arrays.copyOf(this.bytes, this.length);

		// copy the inserted byte
		result[index] = newByte;

		// generate and return result
		return new ByteArray(result);
	}

	@Override
	protected ByteArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new ByteArray(this.bytes, length, rangeOffset, this.reverse, trailer, header,
							 rangeLength, this.bitReversed);
	}

	@Override
	protected ByteArray abstractReverse() {
		// switch trailer and header
		return new ByteArray(this.bytes, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer,
							 this.rangeLength, this.bitReversed);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArray abstractNot() {
		// normalize byte arrays for better performance
		this.normalize();

		// create resulting byte array
		byte[] result = new byte[this.length];

		// perform operation
		for (int i = 0; i < this.length; i++) {
			result[i] = MathUtil.not(this.bytes[i]);
		}
		return new ByteArray(result);
	}

	// This method has been optimized for performance (and is therefore more complicated than necessary)
	@Override
	protected ByteArray abstractAndOrXor(Operator operator, ByteArray other, boolean maxLength, boolean fillBit) {

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
		return new ByteArray(result);
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
		byte mask1 = (leftIndex == leftDiv)
			   ? MathUtil.shiftLeft(ALL_ONE, MathUtil.modulo(leftOffset, Byte.SIZE)) : ALL_ONE;
		byte mask2 = (rightIndex == rightDiv)
			   ? MathUtil.shiftRight(ALL_ONE, MathUtil.modulo(rightOffset, Byte.SIZE)) : ALL_ONE;
		return MathUtil.and(mask1, mask2);
	}

	@Override
	public Class<?> getBaseClass() {
		return ByteArray.class;
	}

}
