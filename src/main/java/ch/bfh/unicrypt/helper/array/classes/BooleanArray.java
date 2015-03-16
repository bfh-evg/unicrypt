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
import ch.bfh.unicrypt.helper.converter.classes.string.BooleanArrayToString;
import java.util.Collection;

/**
 * This class is provides an implementation for immutable arrays of type {@code boolean}/{@code Boolean}. For maximal
 * performance of binary operations, the boolean values are internally stored in a {@link ByteArray} instance. It's
 * sister class {@link ByteArray} is implemented similarly.
 * <p>
 * @see ByteArray
 */
public class BooleanArray
	   extends AbstractBinaryArray<BooleanArray, Boolean> {

	// the internal ByteArray instance containing the boolean values
	private final ByteArray byteArray;

	private BooleanArray(ByteArray byteArray, int length) {
		this(byteArray, length, 0, 0, 0, length);
	}

	private BooleanArray(ByteArray byteArray, int length, int rangeOffset) {
		this(byteArray, length, rangeOffset, 0, 0, length);
	}

	private BooleanArray(ByteArray byteArray, int length, int rangeOffset, int trailer, int header, int rangeLength) {
		// reverse is not used (always false), default value is false
		super(BooleanArray.class, length, rangeOffset, false, false, trailer, header, rangeLength);
		this.byteArray = byteArray;
	}

	/**
	 * Creates a new {@code BooleanArray} instance of a given length with values identical to {@code fillBit}.
	 * <p>
	 * @param fillBit The boolean value contained in the new array
	 * @param length  The length of the new array
	 * @return The new boolean array
	 */
	public static BooleanArray getInstance(boolean fillBit, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		int byteLength = (length + Byte.SIZE - 1) / Byte.SIZE;
		return new BooleanArray(ByteArray.getInstance(fillBit, byteLength), length);
	}

	/**
	 * Creates a new {@code BooleanArray} instance from a given {@code ByteArray} instance. Its length and boolean
	 * values correspond to the bits in the binary representation of the given {@code ByteArray} instance. This
	 * method is a special case of
	 * {@link BooleanArray#getInstance(ch.bfh.unicrypt.helper.array.classes.ByteArrayFinal, int)} with the length
	 * computed automatically.
	 * <p>
	 * @param byteArray The given {@code ByteArray} instance
	 * @return The new boolean array
	 */
	public static BooleanArray getInstance(ByteArray byteArray) {
		if (byteArray == null) {
			throw new IllegalArgumentException();
		}
		return new BooleanArray(byteArray, byteArray.getLength() * Byte.SIZE);
	}

	/**
	 * Creates a new {@code BooleanArray} instance of a given length from a given {@code ByteArray} instance. Its
	 * boolean values correspond to the bits in the binary representation of the {@code byte} values. Extra bits
	 * exceeding the given bit length are ignored.
	 * <p>
	 * @param byteArray The given {@code ByteArray} instance
	 * @param length    The length of the new boolean array
	 * @return The new boolean array
	 */
	public static BooleanArray getInstance(ByteArray byteArray, int length) {
		if (byteArray == null || length < 0 || length > byteArray.getLength() * Byte.SIZE) {
			throw new IllegalArgumentException();
		}
		return new BooleanArray(byteArray, length);
	}

	/**
	 * Creates a new {@code BooleanArray} instance from a given Java array of {@code boolean} values. The Java array is
	 * copied for internal storage. The length and the indices of the values of the resulting array correspond to the
	 * given Java array.
	 * <p>
	 * @param bits The Java array of {@code boolean} values
	 * @return The new boolean array
	 */
	public static BooleanArray getInstance(boolean... bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		int byteLength = (bits.length + Byte.SIZE - 1) / Byte.SIZE;
		byte[] bytes = new byte[byteLength];
		for (int i = 0; i < bits.length; i++) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			if (bits[i]) {
				bytes[byteIndex] = MathUtil.setBit(bytes[byteIndex], bitIndex);
			}
		}
		return new BooleanArray(ByteArray.getInstance(bytes), bits.length);
	}

	/**
	 * Creates a new {@code BooleanArray} instance from a given Java collection. The bits of the collection are copied
	 * for internal storage. The length and the indices of the bits of the resulting array correspond to the given Java
	 * collection.
	 * <p>
	 * @param bits The given Java collection
	 * @return The new array
	 */
	public static BooleanArray getInstance(Collection<Boolean> bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		int byteLength = (bits.size() + Byte.SIZE - 1) / Byte.SIZE;
		byte[] bytes = new byte[byteLength];
		int i = 0;
		for (Boolean b : bits) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			bytes[byteIndex] = MathUtil.replaceBit(bytes[byteIndex], bitIndex, b);
			i++;
		}
		return new BooleanArray(ByteArray.getInstance(bytes), bits.size());
	}

	/**
	 * Transforms a given immutable array of type {@code Boolean} into a {@code BooleanArray} instance. If the given
	 * immutable array is already an instance of {@code BooleanArray}, it is returned without doing anything. Otherwise,
	 * the immutable array is transformed into a {@code ByteArray} instance for internal storage.
	 * <p>
	 * @param immutableArray The given immutable array
	 * @return The new array
	 */
	public static BooleanArray getInstance(ImmutableArray<Boolean> immutableArray) {
		if (immutableArray == null) {
			throw new IllegalArgumentException();
		}
		if (immutableArray instanceof BooleanArray) {
			return (BooleanArray) immutableArray;
		}
		int byteLength = (immutableArray.getLength() + Byte.SIZE - 1) / Byte.SIZE;
		byte[] bytes = new byte[byteLength];
		for (int i = 0; i < immutableArray.getLength(); i++) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			if (immutableArray.getAt(i)) {
				bytes[byteIndex] = MathUtil.setBit(bytes[byteIndex], bitIndex);
			}
		}
		return new BooleanArray(ByteArray.getInstance(bytes), immutableArray.getLength());
	}

	/**
	 * Transforms the {@code BooleanArray} instance into a Java {@code boolean} array.
	 * <p>
	 * @return The resulting Java {@code boolean} array
	 */
	public boolean[] getBooleans() {
		boolean[] result = new boolean[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	private ByteArray getByteArrayFinal() {
		int arrayLength = MathUtil.divide(this.length - 1, Byte.SIZE) + 1;
		byte[] bytes = new byte[arrayLength];
		for (int index = 0; index < arrayLength; index++) {
			bytes[index] = this.byteArray.getByteAt(this.rangeOffset - this.trailer, index, this.rangeOffset, this.rangeLength);
		}
		return ByteArray.getInstance(bytes);
	}

	@Override
	protected String defaultToStringValue() {
		String str = BooleanArrayToString.getInstance().convert(this);
		return "\"" + str + "\"";
	}

	@Override
	protected Boolean abstractGetValueAt(int index) {
		int byteIndex = index / Byte.SIZE;
		int bitIndex = index % Byte.SIZE;
		return MathUtil.getBit(this.byteArray.getAt(byteIndex), bitIndex);
	}

	@Override
	protected BooleanArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new BooleanArray(this.byteArray, length, rangeOffset, trailer, header, rangeLength);
	}

	@Override
	protected BooleanArray abstractInsertAt(int index, Boolean value) {
		BooleanArray bitArray1 = this.extractPrefix(index);
		BooleanArray bitArray2 = this.extract(index, this.length - index);
		if (value) {
			bitArray2 = BooleanArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BooleanArray abstractReplaceAt(int index, Boolean value) {
		BooleanArray bitArray1 = this.extractPrefix(index);
		BooleanArray bitArray2 = this.extract(index + 1, this.length - index - 1);
		if (value) {
			bitArray2 = BooleanArray.getInstance(true).append(bitArray2);
		} else {
			bitArray2 = bitArray2.addPrefix(1);
		}
		return bitArray1.append(bitArray2);
	}

	@Override
	protected BooleanArray abstractAppend(ImmutableArray<Boolean> other) {
		return this.abstractAppend(BooleanArray.getInstance(other));
	}

	protected BooleanArray abstractAppend(BooleanArray other) {
		// make this BooleanArray right-aligned with a ByteArray
		int shift = (Byte.SIZE - this.length % Byte.SIZE) % Byte.SIZE;
		ByteArray byteArray1 = this.addPrefix(shift).getByteArrayFinal();
		ByteArray byteArray2 = other.getByteArrayFinal();
		return new BooleanArray(byteArray1.append(byteArray2), this.length + other.length, shift);
	}

	@Override
	protected BooleanArray abstractReverse() {
		// reverse byteArrary, adjust offset, switch trailer and header (reverse is always false)
		int newOffset = this.byteArray.getLength() * Byte.SIZE - this.rangeOffset - this.rangeLength;
		return new BooleanArray(this.byteArray.byteReverse(), this.length, newOffset, this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected BooleanArray abstractAndOrXor(Operator operator, BooleanArray other, boolean maxLength, boolean fillBit) {
		// make the shorter BooleanArray right-aligned with a ByteArray
		int minLength = Math.min(this.length, other.length);
		int shift = (Byte.SIZE - minLength % Byte.SIZE) % Byte.SIZE;
		ByteArray byteArray1 = this.addPrefix(shift).getByteArrayFinal();
		ByteArray byteArray2 = other.addPrefix(shift).getByteArrayFinal();
		ByteArray result = byteArray1.abstractAndOrXor(operator, byteArray2, maxLength, fillBit);
		int newLength = maxLength ? Math.max(this.length, other.length) : minLength;
		return new BooleanArray(result, newLength, shift);
	}

	@Override
	protected BooleanArray abstractNot() {
		return new BooleanArray(this.getByteArrayFinal().not(), this.length);
	}

}
