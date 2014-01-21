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
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArray
	   extends UniCrypt {

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

	public byte[] getBytes() {
		return Arrays.copyOfRange(bytes, offset, offset + length);
	}

	public byte getByte(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.bytes[this.offset + index];
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
		if (others == null) {
			throw new IllegalArgumentException();
		}
		//Find and remember the length of the longest ByteArray:
		int maxLength = this.length;
		for (ByteArray other : others) {
			if (other == null) {
				throw new IllegalArgumentException();
			}
			maxLength = Math.max(maxLength, other.length);
		}
		//Iterate through the given ByteArrays and xor them into result
		byte[] result = Arrays.copyOf(this.bytes, maxLength);
		for (ByteArray other : others) {
			for (int i = 0; i < other.length; i++) {
				result[i] ^= this.getByte(i);
			}
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
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.getByte(i) & 0xFF));
			delimiter = "|";
		}
		return "\"" + str + "\"";
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 17 * hash + Arrays.hashCode(this.bytes);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ByteArray other = (ByteArray) obj;
		return Arrays.equals(this.bytes, other.bytes);
	}

	public static ByteArray getInstance() {
		return ByteArray.getInstance(0);
	}

	public static ByteArray getInstance(int length) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArray(new byte[length]);
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

	public static ByteArray getRandomInstance(int length, RandomGenerator randomGenerator) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGeneratorCounterMode.DEFAULT_PSEUDO_RANDOM_GENERATOR_COUNTER_MODE;
		}
		return new ByteArray(randomGenerator.nextBytes(length));
	}

}
