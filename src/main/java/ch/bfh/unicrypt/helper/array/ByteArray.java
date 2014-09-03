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
package ch.bfh.unicrypt.helper.array;

import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArray
	   extends AbstractArray<ByteArray>
	   implements Iterable<Byte> {

	public static final int BYTE_ORDER = 1 << Byte.SIZE;
	private static final int BYTE_MASK = BYTE_ORDER - 1;
	private static final byte ALL_ZERO = 0;
	private static final byte ALL_ONE = (byte) BYTE_MASK;

	protected final byte[] bytes;
	private int trailer; // number of trailing zeros not included in bytes
	private int header; // number of leading zeros not included in bytes

	protected ByteArray(byte[] bytes) {
		this(bytes, 0, bytes.length, 0, 0, false);
	}

	protected ByteArray(byte[] bytes, int offset, int length, int trailer, int header, boolean reverse) {
		super(length, offset, reverse);
		this.bytes = bytes;
		this.trailer = trailer;
		this.header = header;
	}

	public byte[] getAll() {
		byte[] result = new byte[this.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	public boolean[] getBits() {
		boolean[] result = new boolean[this.length * Byte.SIZE];
		for (int i = 0; i < result.length; i++) {
			result[i] = this.getBitAt(i);
		}
		return result;
	}

	public byte getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.abstractGetAt(index);
	}

	public boolean getBitAt(int bitIndex) {
		int byteIndex = bitIndex / Byte.SIZE;
		byte mask = bitMask(bitIndex % Byte.SIZE);
		return logicalAND(this.getAt(byteIndex), mask) != 0;
	}

	public int getIntAt(int index) {
		return this.getAt(index) & BYTE_MASK;
	}

	// leading here means the highest indices
	public int countLeadingZeros() {
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.abstractGetAt(i) != 0) {
				return result;
			}
			result++;
		}
		return result;
	}

	// trailing here means the lowest indices
	public int countTrailingZeros() {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (this.abstractGetAt(i) != 0) {
				return result;
			}
			result++;
		}
		return result;
	}

	// leading here means the highest indices
	public int countLeadingZeroBits() {
		int result = 0;
		for (int i = this.length * Byte.SIZE - 1; i >= 0; i--) {
			if (this.getBitAt(i)) {
				return result;
			}
			result++;
		}
		return result;
	}

	// trailing here means the lowest indices
	public int countTrailingZeroBits() {
		int result = 0;
		for (int i = 0; i < this.length * Byte.SIZE; i++) {
			if (this.getBitAt(i)) {
				return result;
			}
			result++;
		}
		return result;
	}

	public int countOneBits() {
		int result = 0;
		for (int i = 0; i < this.length * Byte.SIZE; i++) {
			if (this.getBitAt(i)) {
				result++;
			}
		}
		return result;
	}

	public int countZeroBits() {
		int result = 0;
		for (int i = 0; i < this.length * Byte.SIZE; i++) {
			if (!this.getBitAt(i)) {
				result++;
			}
		}
		return result;
	}

	public ByteArray removeLeadingZeros() {
		int n = this.countLeadingZeros();
		return this.removeSuffix(n);
	}

	public ByteArray removeTrailingZeros() {
		int n = this.countTrailingZeros();
		return this.removePrefix(n);
	}

	// left here means making the byte array smaller
	public ByteArray shiftLeft(int n) {
		if (n < 0) {
			return this.shiftRight(-n);
		}
		return this.removePrefix(Math.min(this.length, n));
	}

	// right here means making the byte array larger
	public ByteArray shiftRight(int n) {
		if (n <= 0) {
			return this.shiftLeft(-n);
		}
		if (this.reverse) {
			return new ByteArray(this.bytes, this.offset, this.length + n, this.trailer, this.header + n, this.reverse);
		} else {
			return new ByteArray(this.bytes, this.offset, this.length + n, this.trailer + n, this.header, this.reverse);
		}
	}

	// left here means making the byte array smaller
	public ByteArray shiftBitsLeft(int n) {
		if (n < 0) {
			return this.shiftBitsRight(-n);
		}
		if (n % Byte.SIZE == 0) {
			return this.shiftLeft(n / Byte.SIZE);
		}
		BitArray bitArray = BitArray.getInstance(this).shiftLeft(n);
		return ByteArray.getInstance(bitArray.getBytes());
	}

	// right here means making the byte array larger
	public ByteArray shiftBitsRight(int n) {
		if (n < 0) {
			return this.shiftBitsLeft(-n);
		}
		if (n % Byte.SIZE == 0) {
			return this.shiftRight(n / Byte.SIZE);
		}
		BitArray bitArray = BitArray.getInstance(this).shiftRight(n);
		return ByteArray.getInstance(bitArray.getBytes());
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
						result[i] = ByteArray.logicalXOR(result[i], b);
						break;
					case 1:
						result[i] = ByteArray.logicalAND(result[i], b);
						break;
					case 2:
						result[i] = ByteArray.logicalOR(result[i], b);
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
		for (int i = 0; i < result.length; i++) {
			result[i] = ByteArray.logicalNOT(this.abstractGetAt(i));
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
		byte[] hash = hashAlgorithm.getHashValue(this.getAll());
		return new ByteArray(hash);
	}

	@Override
	protected String defaultToStringName() {
		return "";
	}

	@Override
	protected String defaultToStringValue() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.length; i++) {
			str = str + delimiter + String.format("%02X", this.getIntAt(i));
			delimiter = "|";
		}
		return "\"" + str + "\"";
	}

	@Override
	public Iterator<Byte> iterator() {
		return new Iterator<Byte>() {

			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < length;
			}

			@Override
			public Byte next() {
				return abstractGetAt(currentIndex++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + this.length;
		for (byte b : this) {
			hash = 79 * hash + b;
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ByteArray)) {
			return false;
		}
		final ByteArray other = (ByteArray) obj;
		if (this.length != other.length) {
			return false;
		}
		for (int i = 0; i < this.length; i++) {
			if (this.abstractGetAt(i) != other.abstractGetAt(i)) {
				return false;
			}
		}
		return true;
	}

	public static ByteArray getInstance() {
		return new ByteArray(new byte[0]);
	}

	public static ByteArray getInstance(int length, boolean fillbit) {
		if (fillbit) {
			return ByteArray.getInstance(length, ALL_ONE);
		} else {
			return ByteArray.getInstance(length, ALL_ZERO);
		}
	}

	public static ByteArray getInstance(int length, byte fillByte) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(new byte[]{fillByte}, 0, length, 0, 0, false);
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
		if (hexString == null || (hexString.length() > 0 && hexString.length() % 3 != 2)) {
			throw new IllegalArgumentException();
		}
		for (int i = 2; i < hexString.length(); i = i + 3) {
			if (hexString.charAt(i) != '|') {
				throw new IllegalArgumentException();
			}
		}
		byte[] bytes = new byte[(hexString.length() + 1) / 3];
		for (int i = 0; i < bytes.length; i++) {
			String string = hexString.substring(i * 3, i * 3 + 2).toUpperCase(Locale.ENGLISH);
			if (!string.matches("[0-9A-F]{2}")) {
				throw new IllegalArgumentException();
			}
			bytes[i] = (byte) Integer.parseInt(string, 16);
		}
		return new ByteArray(bytes);
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
			byteBuffer.put(byteArray.getAll());
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

	protected static byte setBit(byte b, int i) {
		return (byte) logicalOR(b, bitMask(i));
	}

	protected static byte clearBit(byte b, int i) {
		return (byte) logicalAND(b, logicalNOT(bitMask(i)));
	}

	protected static byte bitMask(int i) {
		return (byte) (1 << i);
	}

	protected static byte logicalShiftLeft(byte b, int n) {
		return (byte) (b << n);
	}

	protected static byte logicalShiftRight(byte b, int n) {
		return (byte) (b >>> n);
	}

	protected static byte logicalXOR(byte b1, byte b2) {
		return (byte) (b1 ^ b2);
	}

	protected static byte logicalAND(byte b1, byte b2) {
		return (byte) (b1 & b2);
	}

	protected static byte logicalOR(byte b1, byte b2) {
		return (byte) (b1 | b2);
	}

	protected static byte logicalNOT(byte b) {
		return (byte) ~b;
	}

	@Override
	protected ByteArray abstractExtract(int offset, int length) {
		if (this.reverse) {
			offset = this.length - (offset + length);
		}
		int newHeader = Math.min(Math.max(0, this.trailer - offset), length);
		int newTrailer = Math.min(Math.max(0, this.header - (this.length - offset - length)), length);
		int newOffset = this.offset + Math.max(0, offset - this.trailer);
		return new ByteArray(this.bytes, newOffset, length, newHeader, newTrailer, this.reverse);
	}

	@Override
	protected ByteArray abstractAppend(ByteArray other) {
		byte[] result = new byte[this.length + other.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		for (int i = 0; i < other.length; i++) {
			result[this.length + i] = other.abstractGetAt(i);
		}
		return new ByteArray(result);
	}

	@Override
	protected ByteArray abstractReverse() {
		return new ByteArray(this.bytes, this.offset, this.length, this.trailer, this.header, !this.reverse);
	}

	private byte abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		if (index < this.trailer || index >= this.length - this.header) {
			return ByteArray.ALL_ZERO;
		}
		return this.bytes[(this.offset + index - this.trailer) % this.bytes.length];
	}

	@Override
	protected Class<ByteArray> getArrayClass() {
		return ByteArray.class;
	}

}
