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
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayNewToString;
import java.util.Collection;

/**
 * This class is provides an implementation for immutable arrays of type {@code byte}/{@code Byte}. For maximal
 * performance of binary operations, the byte values are internally stored in a {@link LongArray} instance. It's sister
 * class {@link BooleanArray} is implemented similarly.
 * <p>
 * @see BooleanArray
 */
public class ByteArrayNew
	   extends AbstractBinaryArray<ByteArrayNew, Byte> {

	// two static variables for the two borderline long values
	private static final byte ALL_ZERO = (byte) 0x00;
	private static final byte ALL_ONE = (byte) 0xFF;
	private static final int LONG_SIZE_IN_BYTES = Long.SIZE / Byte.SIZE;

	// the internal LongArray instance containing the bytes
	private final LongArray longArray;

	private ByteArrayNew(LongArray longArray, int length) {
		this(longArray, length, 0, 0, 0, length);
	}

	private ByteArrayNew(LongArray longArray, int length, int rangeOffset) {
		this(longArray, length, rangeOffset, 0, 0, length);
	}

	private ByteArrayNew(LongArray longArray, int length, int rangeOffset, int trailer, int header, int rangeLength) {
		// reverse is not used (always false), default value is ALL_ZERO,
		super(ByteArrayNew.class, length, rangeOffset, false, ALL_ZERO, trailer, header, rangeLength);
		this.longArray = longArray;
	}

	/**
	 * Creates a new {@code ByteArrayNew} instance of a given length. Depending on the parameter {@code fillBit}, all
	 * its values are identical to either {@code ALL_ZERO} or {@code ALL_ONE}. This method is a special case of
	 * {@link ByteArrayNew#getInstance(byte, int)}.
	 * <p>
	 * @param fillBit A flag indicating whether the values in the new array are {@code ALL_ZERO} or {@code ALL_ONE}
	 * @param length  The length of the new array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(boolean fillBit, int length) {
		if (fillBit) {
			return ByteArrayNew.getInstance(ALL_ONE, length);
		} else {
			return ByteArrayNew.getInstance(ALL_ZERO, length);
		}
	}

	/**
	 * Creates a new {@code ByteArrayNew} instance of a given length. All its values are identical to the given byte.
	 * <p>
	 * @param fillByte The byte included in the new array
	 * @param length   The length of the new array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(byte fillByte, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		int longLength = (length + LONG_SIZE_IN_BYTES - 1) / LONG_SIZE_IN_BYTES;
		long fillLong = MathUtil.fillWithByte(fillByte);
		return new ByteArrayNew(LongArray.getInstance(fillLong, longLength), length);
	}

	/**
	 * Creates a new {@code ByteArray} instance from a given {@code LongArray} instance. Its bytes correspond to the
	 * bits in the binary representation of the given {@code LongArray} instance. This method is a special case of
	 * {@link ByteArray#getInstance(ch.bfh.unicrypt.helper.array.classes.LongArray, int)} with the length computed
	 * automatically.
	 * <p>
	 * @param longArray The given {@code LongArray} instance
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(LongArray longArray) {
		if (longArray == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayNew(longArray, longArray.getLength() * LONG_SIZE_IN_BYTES);
	}

	/**
	 * Creates a new {@code ByteArray} instance of a given length from a given {@code LongArray} instance. Its bytes
	 * correspond to the the bits in the binary representation of the {@code long} values. Extra bits exceeding the
	 * given byte length are ignored.
	 * <p>
	 * @param longArray The given {@code LongArray} instance
	 * @param length    The length of the new byte array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(LongArray longArray, int length) {
		if (longArray == null || length < 0 || length > longArray.getLength() * LONG_SIZE_IN_BYTES) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayNew(longArray, length);
	}

	/**
	 * Creates a new {@code ByteArray} instance from a given Java byte array. The bytes are copied for internal storage.
	 * The length and the indices of the bytes of the resulting array correspond to the given Java byte array.
	 * <p>
	 * @param bytes The Java byte array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		int longLength = (bytes.length + LONG_SIZE_IN_BYTES - 1) / LONG_SIZE_IN_BYTES;
		long[] longs = new long[longLength];
		for (int i = 0; i < bytes.length; i++) {
			int longIndex = i / LONG_SIZE_IN_BYTES;
			int byteIndex = i % LONG_SIZE_IN_BYTES;
			longs[longIndex] = MathUtil.replaceByte(longs[longIndex], byteIndex, bytes[i]);
		}
		return new ByteArrayNew(LongArray.getInstance(longs), bytes.length);
	}

	/**
	 * Creates a new {@code ByteArray} instance from a given Java collection. The bytes of the collection are copied for
	 * internal storage. The length and the indices of the bytes of the resulting array correspond to the given Java
	 * collection.
	 * <p>
	 * @param bytes The given Java collection
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(Collection<Byte> bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		int longLength = (bytes.size() + LONG_SIZE_IN_BYTES - 1) / LONG_SIZE_IN_BYTES;
		long[] longs = new long[longLength];
		int i = 0;
		for (Byte b : bytes) {
			int longIndex = i / LONG_SIZE_IN_BYTES;
			int byteIndex = i % LONG_SIZE_IN_BYTES;
			longs[longIndex] = MathUtil.replaceByte(longs[longIndex], byteIndex, b);
			i++;
		}
		return new ByteArrayNew(LongArray.getInstance(longs), bytes.size());
	}

	/**
	 * This is a convenience method to avoid casting {@code int} to {@code byte} values when calling
	 * {@link ByteArrayNew#getInstance(byte...)}. It checks if all {@code int} values in the given Java array are in the
	 * allowed range, casts them to {@code byte} values, and uses them to call
	 * {@link ByteArrayNew#getInstance(byte...)}.
	 * <p>
	 * @see ByteArrayNew#getInstance(byte...)
	 * @param integers The Java {@code int} array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(int... integers) {
		if (integers == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[integers.length];
		int i = 0;
		for (int integer : integers) {
			if (integer < 0x00 || integer > 0xFF) {
				throw new IllegalArgumentException();
			}
			bytes[i++] = (byte) integer;
		}
		return ByteArrayNew.getInstance(bytes);
	}

	/**
	 * Transforms a given immutable array of type {@code Byte} into a {@code ByteArray} instance. If the given immutable
	 * array is already an instance of {@code ByteArray}, it is returned without doing anything. Otherwise, the
	 * immutable array is transformed into a {@code LongArray} instance for internal storage.
	 * <p>
	 * @param immutableArray The given immutable array
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(ImmutableArray<Byte> immutableArray) {
		if (immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof ByteArrayNew) {
			return (ByteArrayNew) immutableArray;
		}
		int longLength = (immutableArray.getLength() + LONG_SIZE_IN_BYTES - 1) / LONG_SIZE_IN_BYTES;
		long[] longs = new long[longLength];
		for (int i = 0; i < immutableArray.getLength(); i++) {
			int longIndex = i / LONG_SIZE_IN_BYTES;
			int byteIndex = i % LONG_SIZE_IN_BYTES;
			longs[longIndex] = MathUtil.replaceByte(longs[longIndex], byteIndex, immutableArray.getAt(i));
		}
		return new ByteArrayNew(LongArray.getInstance(longs), immutableArray.getLength());
	}

	// convenience method to construct byte arrays by hex strings (e.g. "03|A2|29|FF|96")
	/**
	 * This is a convenience method to construct byte arrays by hex strings, e.g., {@code "03|A2|29|FF|96"} for a byte
	 * array of length 5 with hex values 03, A2, 29, FF, 96.
	 * <p>
	 * @param hexString The given hex string
	 * @return The new byte array
	 */
	public static ByteArrayNew getInstance(String hexString) {
		if (hexString == null) {
			throw new IllegalArgumentException();
		}
		return ByteArrayNewToString.getInstance(ByteArrayNewToString.Radix.HEX, "|").reconvert(hexString);
	}

	/**
	 * Transforms the {@code ByteArray} instance into a Java {@code byte} array.
	 * <p>
	 * @return The resulting Java {@code byte} array
	 */
	public byte[] getBytes() {
		byte[] result = new byte[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	private LongArray getLongArray() {
		int arrayLength = MathUtil.divide(this.length - 1, LONG_SIZE_IN_BYTES) + 1;
		long[] longs = new long[arrayLength];
		for (int index = 0; index < arrayLength; index++) {
			longs[index] = this.longArray.getLongAt(this.rangeOffset - this.trailer, index, this.rangeOffset, this.rangeLength);
		}
		return LongArray.getInstance(longs);
	}

	@Override
	protected String defaultToStringValue() {
		// TODO
//		String str = ByteArrayToString.getInstance().convert(this);
		String str = "";
		return "\"" + str + "\"";
	}

	@Override
	protected Byte abstractGetValueAt(int index) {
		int longIndex = index / LONG_SIZE_IN_BYTES;
		int byteIndex = index % LONG_SIZE_IN_BYTES;
		return MathUtil.getByte(this.longArray.getAt(longIndex), byteIndex);
	}

	@Override
	protected ByteArrayNew abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new ByteArrayNew(this.longArray, length, rangeOffset, trailer, header, rangeLength);
	}

	@Override
	protected ByteArrayNew abstractInsertAt(int index, Byte value) {
		ByteArrayNew byteArray1 = this.extractPrefix(index);
		ByteArrayNew byteArray2 = ByteArrayNew.getInstance(value);
		ByteArrayNew byteArray3 = this.extract(index, this.length - index);
		return byteArray1.append(byteArray2.append(byteArray3));
	}

	@Override
	protected ByteArrayNew abstractReplaceAt(int index, Byte value) {
		ByteArrayNew byteArray1 = this.extractPrefix(index);
		ByteArrayNew byteArray2 = ByteArrayNew.getInstance(value);
		ByteArrayNew byteArray3 = this.extract(index + 1, this.length - index - 1);
		return byteArray1.append(byteArray2.append(byteArray3));
	}

	@Override
	protected ByteArrayNew abstractAppend(ImmutableArray<Byte> other) {
		return this.abstractAppend(ByteArrayNew.getInstance(other));
	}

	protected ByteArrayNew abstractAppend(ByteArrayNew other) {
		// make this ByteArray right-aligned with a LongArray
		int shift = (LONG_SIZE_IN_BYTES - this.length % LONG_SIZE_IN_BYTES) % LONG_SIZE_IN_BYTES;
		LongArray longArray1 = this.addPrefix(shift).getLongArray();
		LongArray longArray2 = other.getLongArray();
		return new ByteArrayNew(longArray1.append(longArray2), this.length + other.length, shift);
	}

	@Override
	protected ByteArrayNew abstractReverse() {
		// reverse longArrarangeOffsetust offset, switch trailer and header (reverse is always false)
		int newOffset = this.longArray.getLength() * LONG_SIZE_IN_BYTES - this.rangeOffset - this.rangeLength;
		return new ByteArrayNew(this.longArray.bitReverse(), this.length, newOffset, this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected ByteArrayNew abstractAndOrXor(Operator operator, ByteArrayNew other, boolean maxLength, boolean fillBit) {
		// make the shorter ByteArray right-aligned with a LongArray
		int minLength = Math.min(this.length, other.length);
		int shift = (LONG_SIZE_IN_BYTES - minLength % LONG_SIZE_IN_BYTES) % LONG_SIZE_IN_BYTES;
		LongArray longArray1 = this.addPrefix(shift).getLongArray();
		LongArray longArray2 = other.addPrefix(shift).getLongArray();
		LongArray result = longArray1.abstractAndOrXor(operator, longArray2, maxLength, fillBit);
		int newLength = maxLength ? Math.max(this.length, other.length) : minLength;
		return new ByteArrayNew(result, newLength, shift);
	}

	@Override
	protected ByteArrayNew abstractNot() {
		return new ByteArrayNew(this.getLongArray().not(), this.length);
	}

}
