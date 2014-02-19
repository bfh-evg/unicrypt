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
package ch.bfh.unicrypt.math.helper;

import ch.bfh.unicrypt.crypto.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomByteSequence;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArray
	   extends UniCrypt
	   implements Iterable<Byte> {

	private static final byte ALL_ZERO = (byte) 0x00;
	private static final byte ALL_ONE = (byte) 0xff;

	private final byte[] bytes;
	private final int offset;
	private final int length;

	protected ByteArray(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}

	protected ByteArray(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}

	public int getLength() {
		return this.length;
	}

	public byte[] getAll() {
		return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
	}

	public byte getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.bytes[this.offset + index];
	}

	public ByteArray extract(int length) {
		return this.extract(0, length);
	}

	public ByteArray extract(int offset, int length) {
		if (offset < 0 || length < 0 || offset + length > this.length) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(this.bytes, this.offset + offset, length);
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
		byte[] result = new byte[this.length + other.length];
		System.arraycopy(this.bytes, this.offset, result, 0, this.length);
		System.arraycopy(other.bytes, other.offset, result, this.length, other.length);
		return new ByteArray(result);
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
						result[i] ^= b;
						break;
					case 1:
						result[i] &= b;
						break;
					case 2:
						result[i] |= b;
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
			result[i] = (byte) (~this.getAt(i) & 0xff);
		}
		return new ByteArray(result);
	}

	public ByteArray getHash() {
		return this.getHash(HashMethod.DEFAULT);
	}

	public ByteArray getHash(HashMethod hashMethod) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(hashMethod.getMessageDigest().digest(bytes));
	}

	@Override
	public String defaultToStringValue() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.length; i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.getAt(i) & 0xff));
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
		return new ByteArray(new byte[length]);
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
			if (integer < 0 || integer > 256) {
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
			String string = hexString.substring(i * 3, i * 3 + 2);
			if (!string.matches("[0-9A-F]{2}")) {
				throw new IllegalArgumentException();
			}
			bytes[i] = (byte) Integer.parseInt(string, 16);
		}
		return new ByteArray(bytes);
	}

	public static ByteArray getRandomInstance(int length) {
		return ByteArray.getRandomInstance(length, null);
	}

	public static ByteArray getRandomInstance(int length, RandomByteSequence randomByteSequence) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		if (randomByteSequence == null) {
			randomByteSequence = HybridRandomByteSequence.getInstance();
		}
		return randomByteSequence.getNextByteArray(length);
	}

}
