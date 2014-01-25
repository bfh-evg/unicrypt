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

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGeneratorCounterMode;
import ch.bfh.unicrypt.crypto.random.classes.RandomNumberGenerator;
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
		return Arrays.copyOfRange(bytes, offset, offset + length);
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
			if (currentIndex < lastIndex || currentIndex >= this.length) {
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
		return this.applyOperand(0, others);
	}

	public ByteArray and(ByteArray... others) {
		return this.applyOperand(1, others);
	}

	public ByteArray or(ByteArray... others) {
		return this.applyOperand(2, others);
	}

	private ByteArray applyOperand(int operand, ByteArray... others) {
		if (others == null) {
			throw new IllegalArgumentException();
		}
		int minLength = this.length;
		for (ByteArray other : others) {
			if (other == null) {
				throw new IllegalArgumentException();
			}
			minLength = Math.min(minLength, other.length);
		}
		byte[] result = Arrays.copyOf(this.bytes, minLength);
		for (ByteArray other : others) {
			for (int i = 0; i < minLength; i++) {
				switch (operand) {
					case 0:
						result[i] ^= other.getAt(i);
						break;
					case 1:
						result[i] &= other.getAt(i);
						break;
					case 2:
						result[i] |= other.getAt(i);
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
		for (int i = 0; i < this.length; i++) {
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
	public String standardToStringContent() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.length; i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.getAt(i) & 0xFF));
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
		if (obj == null || this.getClass() != obj.getClass()) {
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
		return ByteArray.getInstance(0);
	}

	public static ByteArray getInstance(int length) {
		return ByteArray.getInstance(length, false);
	}

	public static ByteArray getInstance(int length, boolean bit) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[length];
		if (bit) {
			Arrays.fill(bytes, (byte) 0xff);
		}
		return new ByteArray(new byte[length]);
	}

	public static ByteArray getInstance(int... integers) {
		if (integers == null) {
			throw new IllegalArgumentException();
		}
		byte[] bytes = new byte[integers.length];
		int i = 0;
		for (int integer : integers) {
			if (integer < 0x00 || integer > 0xff) {
				throw new IllegalArgumentException();
			}
			bytes[i++] = (byte) integer;
		}
		return new ByteArray(bytes);
	}

	public static ByteArray getInstance(byte[] bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(bytes.clone());
	}

	public static ByteArray getRandomInstance(int length) {
		return ByteArray.getRandomInstance(length, null);
	}

	public static ByteArray getRandomInstance(int length, RandomNumberGenerator randomGenerator) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGeneratorCounterMode.DEFAULT_PSEUDO_RANDOM_GENERATOR_COUNTER_MODE;
		}
		return new ByteArray(randomGenerator.nextBytes(length));
	}

}
