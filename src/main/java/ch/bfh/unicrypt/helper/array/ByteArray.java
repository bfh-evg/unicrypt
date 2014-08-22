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

import ch.bfh.unicrypt.helper.UniCrypt;
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
	   extends UniCrypt
	   implements Iterable<Byte> {

	public static final int BYTE_ORDER = 1 << Byte.SIZE;
	private static final int BYTE_MASK = BYTE_ORDER - 1;
	private static final byte ALL_ZERO = 0;
	private static final byte ALL_ONE = (byte) BYTE_MASK;

	protected final byte[] bytes;
	private final int offset;
	private final int length;
	private boolean reverse;

	protected ByteArray(byte[] bytes) {
		this(bytes, 0, bytes.length, false);
	}

	protected ByteArray(byte[] bytes, int offset, int length, boolean reverse) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
		this.reverse = reverse;
	}

	public int getLength() {
		return this.length;
	}

	public byte[] getAll() {
		byte[] bytes = Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
		if (this.reverse) {
			reverse(bytes);
		}
		return bytes;
	}

	public byte getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (this.reverse) {
			index = this.length - index - 1;
		}
		return this.bytes[this.offset + index];
	}

	public int getIntAt(int index) {
		return this.getAt(index) & BYTE_MASK;
	}

	public boolean getBitAt(int bitIndex) {
		int byteIndex = bitIndex / Byte.SIZE;
		return (this.getAt(byteIndex) & (1 << (bitIndex % Byte.SIZE))) != 0;
	}

	// leading here means the highest indices
	public int countLeadingZeroBytes() {
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.getAt(i) > 0) {
				return result;
			}
			result++;
		}
		return result;
	}

	// trailing here means the lowest indices
	public int countTrailingZeroBytes() {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (this.getAt(i) > 0) {
				return result;
			}
			result++;
		}
		return result;
	}

	public ByteArray stripLeadingZeroBytes() {
		int n = this.countLeadingZeroBytes();
		return this.stripSuffix(n);
	}

	public ByteArray stripTrailingZeroBytes() {
		int n = this.countTrailingZeroBytes();
		return this.stripPrefix(n);
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

	public int countBits() {
		int result = 0;
		for (int i = 0; i < this.length * Byte.SIZE; i++) {
			if (this.getBitAt(i)) {
				result++;
			}
		}
		return result;
	}

	// prefix here means the lowest indices
	public ByteArray stripPrefix(int n) {
		return this.extract(n, this.length - n);
	}

	// trailing here means the highest indices
	public ByteArray stripSuffix(int n) {
		return this.extract(0, this.length - n);
	}

	public ByteArray extractPrefix(int length) {
		return this.extract(0, length);
	}

	public ByteArray extractSuffix(int length) {
		return this.extract(this.length - length, length);
	}

	public ByteArray extractRange(int fromIndex, int toIndex) {
		return this.extract(fromIndex, toIndex - fromIndex + 1);
	}

	public ByteArray extract(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > this.length) {
			throw new IllegalArgumentException();
		}
		if (offset == 0 && length == this.length) {
			return this;
		}
		if (this.reverse) {
			offset = this.length - length - offset;
		}
		return new ByteArray(this.bytes, this.offset + offset, length, this.reverse);
	}

	public ByteArray[] split(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		ByteArray[] result = new ByteArray[indices.length + 1];
		int lastIndex = 0;
		for (int i = 0; i < indices.length; i++) {
			int currentIndex = indices[i];
			if (currentIndex < lastIndex || currentIndex > this.length) {
				throw new IllegalArgumentException();
			}
			result[i] = this.extract(lastIndex, currentIndex - lastIndex);
			lastIndex = currentIndex;
		}
		result[indices.length] = this.extract(lastIndex, this.length - lastIndex);
		return result;
	}

	public ByteArray concatenate(ByteArray other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		if (this.length == 0) {
			return other;
		}
		if (other.length == 0) {
			return this;
		}
		byte[] result = new byte[this.length + other.length];
		System.arraycopy(this.bytes, this.offset, result, 0, this.length);
		if (this.reverse) {
			reverse(result, 0, this.length - 1);
		}
		System.arraycopy(other.bytes, other.offset, result, this.length, other.length);
		if (other.reverse) {
			reverse(result, this.length, this.length + other.length - 1);
		}
		return new ByteArray(result);
	}

	public ByteArray reverse() {
		return new ByteArray(this.bytes, this.offset, this.length, !this.reverse);
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
			result[i] = (i < this.length) ? this.getAt(i) : fillByte;
		}
		for (ByteArray other : others) {
			for (int i = 0; i < result.length; i++) {
				byte b = (i < other.length) ? other.getAt(i) : fillByte;
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
			result[i] = ByteArray.logicalNOT(this.getAt(i));
		}
		return new ByteArray(result);
	}

	// left here means making the byte array smaller
	public ByteArray shiftLeft(int n) {
		if (n == 0) {
			return this;
		}
		if (n < 0) {
			return this.shiftRight(-n);
		}
		int nBytes = n / Byte.SIZE;
		int nBits = n % Byte.SIZE;
		int nBits2 = Byte.SIZE - nBits;
		int newBitLength = (this.length * Byte.SIZE - countLeadingZeroBits() - n);
		if (newBitLength <= 0) {
			return new ByteArray(new byte[0]);
		}
		int newByteLength = newBitLength / Byte.SIZE + ((newBitLength % Byte.SIZE == 0) ? 0 : 1);
		byte[] result = new byte[newByteLength];
		for (int i = 0; i < newByteLength; i++) {
			result[i] = (byte) (((this.getAt(i + nBytes) & 0xff) >>> nBits) | (i + nBytes + 1 < this.length ? (this.getAt(i + nBytes + 1) << nBits2) : 0));
		}
		return new ByteArray(result);
	}

	// right here means making the byte array larger
	public ByteArray shiftRight(int n) {
		if (n == 0) {
			return this;
		}
		if (n < 0) {
			return this.shiftLeft(-n);
		}
		int nBytes = n / Byte.SIZE;
		int nBits = n % Byte.SIZE;
		int nBits2 = Byte.SIZE - nBits;
		int newBitLength = (this.length * Byte.SIZE - countLeadingZeroBits() + n);
		int newByteLength = newBitLength / Byte.SIZE + ((newBitLength % Byte.SIZE == 0) ? 0 : 1);
		byte[] result = new byte[newByteLength];
		for (int i = 0; i < newByteLength; i++) {
			if (i < nBytes) {
				result[i] = 0;
			} else {
				result[i] = (byte) (((i - nBytes < this.length ? (this.getAt(i - nBytes) << nBits) : 0) | (i - nBytes - 1 >= 0 ? ((this.getAt(i - nBytes - 1) & 0xff) >>> nBits2) : 0)));
			}
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
		byte[] hash;
		if (this.reverse) {
			hash = hashAlgorithm.getHashValue(this.getAll());
		} else {
			hash = hashAlgorithm.getHashValue(this.bytes, this.offset, this.length);
		}
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
				return getAt(currentIndex++);
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
			if (this.getAt(i) != other.getAt(i)) {
				return false;
			}
		}
		return true;
	}

	public static ByteArray getInstance() {
		return new ByteArray(new byte[0]);
	}

	public static ByteArray getInstance(int length, boolean fillbit) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[length];
		if (fillbit) {
			Arrays.fill(bytes, ALL_ONE);
		}
		return new ByteArray(bytes);
	}

	public static ByteArray getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(bytes.clone());
	}

	// convenicene method to avoid casting integers to byte
	public static ByteArray getInstance(int... integers) {
		if (integers == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[integers.length];
		int i = 0;
		for (int integer : integers) {
			if (integer < 0 || integer >= (1 << Byte.SIZE)) {
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
			byteBuffer.put(byteArray.bytes, byteArray.offset, byteArray.length);
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

	private static byte logicalXOR(byte b1, byte b2) {
		return (byte) (b1 ^ b2);
	}

	private static byte logicalAND(byte b1, byte b2) {
		return (byte) (b1 & b2);
	}

	private static byte logicalOR(byte b1, byte b2) {
		return (byte) (b1 | b2);
	}

	private static byte logicalNOT(byte b) {
		return (byte) ~b;
	}

	private static void reverse(byte[] bytes) {
		reverse(bytes, 0, bytes.length - 1);
	}

	private static void reverse(byte[] bytes, int i, int j) {
		byte tmp;
		while (j > i) {
			tmp = bytes[j];
			bytes[j] = bytes[i];
			bytes[i] = tmp;
			j--;
			i++;
		}
	}

}
