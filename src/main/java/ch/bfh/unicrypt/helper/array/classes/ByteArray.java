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
import ch.bfh.unicrypt.helper.array.abstracts.AbstractDefaultValueArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.converter.classes.string.ByteArrayToString;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArray
	   extends AbstractDefaultValueArray<ByteArray, Byte> {

	public static final int BYTE_ORDER = 1 << Byte.SIZE;
	private static final int BYTE_MASK = BYTE_ORDER - 1;
	private static final byte ALL_ZERO = 0;
	private static final byte ALL_ONE = (byte) BYTE_MASK;

	protected final byte[] bytes;
	protected BitArray bitArray; // used to provide some bit operations

	protected ByteArray(byte fillByte, int arrayLength) {
		this(new byte[]{fillByte}, arrayLength, 0, false, 0, 0, arrayLength);
	}

	protected ByteArray(byte[] bytes) {
		this(bytes, bytes.length, 0, false, 0, 0, bytes.length);
	}

	protected ByteArray(byte[] bytes, int arrayLength, int rangeOffset, boolean reverse, int trailer, int header, int rangeLength) {
		super(ByteArray.class, ByteArray.ALL_ZERO, arrayLength, rangeOffset, rangeLength, trailer, header, reverse);
		this.bytes = bytes;
		if (bytes.length <= 1 && trailer == 0 && header == 0) {
			this.uniform = true;
		}
	}

	public byte getByteAt(int index) {
		return this.getAt(index);
	}

	public BitArray getBitArray() {
		if (this.bitArray == null) {
			this.bitArray = BitArray.getInstance(this);
		}
		return this.bitArray;
	}

	public int getBitLength() {
		return this.getBitArray().getLength();
	}

	public byte[] getBytes() {
		byte[] result = new byte[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	public boolean getBitAt(int bitIndex) {
		return this.getBitArray().getAt(bitIndex);
	}

	public int getIntAt(int index) {
		return this.getAt(index) & BYTE_MASK;
	}

	// leading here means the highest indices
	public int countLeadingZeroBits() {
		return this.getBitArray().countSuffix();
	}

	// trailing here means the lowest indices
	public int countTrailingZeroBits() {
		return this.getBitArray().countPrefix();
	}

	public int countOneBits() {
		return this.getBitArray().count(true);
	}

	public int countZeroBits() {
		return this.getBitArray().count(false);
	}

	// left here means making the byte array smaller
	public ByteArray shiftBitsLeft(int n) {
		if (n < 0) {
			return this.shiftBitsRight(-n);
		}
		if (n % Byte.SIZE == 0) {
			return this.shiftLeft(n / Byte.SIZE);
		}
		return ByteArray.getInstance(this.getBitArray().shiftLeft(n).getBytes());
	}

	// right here means making the byte array larger
	public ByteArray shiftBitsRight(int n) {
		if (n < 0) {
			return this.shiftBitsLeft(-n);
		}
		if (n % Byte.SIZE == 0) {
			return this.shiftRight(n / Byte.SIZE);
		}
		return ByteArray.getInstance(this.getBitArray().shiftRight(n).getBytes());
	}

	public ByteArray xor(ByteArray... others) {
		return this.applyOperand(0, others, false, ALL_ZERO);
	}

	public ByteArray and(ByteArray... others) {
		return this.applyOperand(1, others, false, ALL_ZERO);
	}

	public ByteArray or(ByteArray... others) {
		return this.applyOperand(2, others, false, ALL_ZERO);
	}

	public ByteArray xorFillZero(ByteArray... others) {
		return this.applyOperand(0, others, true, ALL_ZERO);
	}

	public ByteArray andFillZero(ByteArray... others) {
		return this.applyOperand(1, others, true, ALL_ZERO);
	}

	public ByteArray orFillZero(ByteArray... others) {
		return this.applyOperand(2, others, true, ALL_ZERO);
	}

	public ByteArray xorFillOne(ByteArray... others) {
		return this.applyOperand(0, others, true, ALL_ONE);
	}

	public ByteArray andFillOne(ByteArray... others) {
		return this.applyOperand(1, others, true, ALL_ONE);
	}

	public ByteArray orFillOne(ByteArray... others) {
		return this.applyOperand(2, others, true, ALL_ONE);
	}

	private ByteArray applyOperand(int operand, ByteArray[] others, boolean maxLength, byte fillByte) {
		if (others == null) {
			throw new IllegalArgumentException();
		}
		int newLength = this.length;
		for (ByteArray other : others) {
			if (other == null) {
				throw new IllegalArgumentException();
			}
			newLength = (maxLength) ? Math.max(newLength, other.length) : Math.min(newLength, other.length);
		}
		byte[] result = new byte[newLength];
		for (int i = 0; i < result.length; i++) {
			result[i] = (i < this.length) ? this.abstractGetAt(i) : fillByte;
		}
		for (ByteArray other : others) {
			for (int i = 0; i < result.length; i++) {
				byte b = (i < other.length) ? other.abstractGetAt(i) : fillByte;
				switch (operand) {
					case 0:
						result[i] = MathUtil.xor(result[i], b);
						break;
					case 1:
						result[i] = MathUtil.and(result[i], b);
						break;
					case 2:
						result[i] = MathUtil.or(result[i], b);
						break;
					default:
						throw new UnsupportedOperationException();
				}
			}
		}
		return new ByteArray(result);
	}

	public ByteArray not() {
		byte[] result = new byte[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = MathUtil.not(this.abstractGetAt(i));
		}
		return new ByteArray(result);
	}

	public ByteArray getHashValue() {
		return this.getHashValue(HashAlgorithm.getInstance());
	}

	public ByteArray getHashValue(HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		byte[] hash = hashAlgorithm.getHashValue(this.getBytes());
		return new ByteArray(hash);
	}

	@Override
	protected String defaultToStringValue() {
		String str = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "|").convert(this);
		return "\"" + str + "\"";
	}

	public static ByteArray getInstance() {
		return new ByteArray(new byte[0]);
	}

	public static ByteArray getInstance(boolean fillbit, int length) {
		if (fillbit) {
			return ByteArray.getInstance(ALL_ONE, length);
		} else {
			return ByteArray.getInstance(ALL_ZERO, length);
		}
	}

	public static ByteArray getInstance(byte fillByte, int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(fillByte, length);
	}

	public static ByteArray getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(Arrays.copyOf(bytes, bytes.length));
	}

	// convenicene method to avoid casting integers to byte
	public static ByteArray getInstance(int... integers) {
		if (integers == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[integers.length];
		int i = 0;
		for (int integer : integers) {
			if (integer < 0 || integer >= ByteArray.BYTE_ORDER) {
				throw new IllegalArgumentException();
			}
			bytes[i++] = (byte) integer;
		}
		return new ByteArray(bytes);
	}

	// convenience method to construct byte arrays by hex strings (e.g. "03|A2|29|FF|96")
	public static ByteArray getInstance(String hexString) {
		if (hexString == null) {
			throw new IllegalArgumentException();
		}
		return ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, "|").reconvert(hexString);
	}

	public static ByteArray getInstance(List<Byte> byteList) {
		if (byteList == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[byteList.size()];
		int i = 0;
		for (Byte b : byteList) {
			if (b == null) {
				throw new IllegalArgumentException();
			}
			bytes[i] = b;
			i++;
		}
		return new ByteArray(bytes);
	}

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
		ByteBuffer byteBuffer = ByteBuffer.allocate(length);
		for (ByteArray byteArray : byteArrays) {
			byteBuffer.put(byteArray.getBytes());
		}
		return new ByteArray(byteBuffer.array());
	}

	public static ByteArray getRandomInstance(int length) {
		return ByteArray.getRandomInstance(length, HybridRandomByteSequence.getInstance());
	}

	public static ByteArray getRandomInstance(int length, RandomByteSequence randomByteSequence) {
		if (length < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return randomByteSequence.getNextByteArray(length);
	}

	@Override
	protected ByteArray abstractAppend(ImmutableArray<Byte> other) {
		byte[] result = new byte[this.length + other.getLength()];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		for (int i : other.getAllIndices()) {
			result[this.length + i] = other.getAt(i);
		}
		return new ByteArray(result);
	}

	@Override
	protected ByteArray abstractInsertAt(int index, Byte newByte) {
		byte[] result = new byte[this.length + 1];
		for (int i : this.getAllIndices()) {
			if (i < index) {
				result[i] = this.abstractGetAt(i);
			} else {
				result[i + 1] = this.abstractGetAt(i);
			}
		}
		result[index] = newByte;
		return new ByteArray(result);
	}

	@Override
	protected ByteArray abstractReplaceAt(int index, Byte newByte) {
		byte[] result = new byte[this.length];
		for (int i : this.getAllIndices()) {
			result[i] = this.abstractGetAt(i);
		}
		result[index] = newByte;
		return new ByteArray(result);
	}

	@Override
	protected ByteArray abstractReverse() {
		// switch trailer and header
		return new ByteArray(this.bytes, this.length, this.rangeOffset, !this.reverse, this.header, this.trailer, this.rangeLength);
	}

	@Override
	protected ByteArray abstractGetInstance(int length, int rangeOffset, int rangeLength, int trailer, int header) {
		return new ByteArray(this.bytes, length, rangeOffset, this.reverse, trailer, header, rangeLength);
	}

	@Override
	protected Byte abstractGetValueAt(int index) {
		return this.bytes[index % this.bytes.length];
	}

}
