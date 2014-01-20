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

	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	private final byte[] bytes;

	protected ByteArray(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return this.bytes.clone();
	}

	public int getLength() {
		return this.bytes.length;
	}

	public byte getByte(int index) {
		if (index < 0 || index >= this.getLength()) {
			throw new IndexOutOfBoundsException();
		}
		return this.bytes[index];
	}

	public ByteArray concatenate(ByteArray other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		byte[] result = Arrays.copyOf(this.bytes, this.getLength() + other.getLength());
		System.arraycopy(other.bytes, 0, result, this.getLength(), other.getLength());
		return new ByteArray(result);
	}

	public ByteArray xor(ByteArray... others) {

		//Find and remember the length of the longest ByteArray:
		int maxLength = this.bytes.length;
		for (ByteArray array : others) {
			if (array != null) {
				maxLength = Math.max(maxLength, array.bytes.length);
			}
		}
		//Create the xoredBytes with a size similar to the found maxLength
		//Put the bytes of this ByteArray directly into the new xoredBytes
		byte[] xoredBytes = Arrays.copyOf(this.bytes, maxLength);

		//Iterate through the other ByteArrays and xor them into the xoredBytes
		for (ByteArray array : others) {
			if (array != null) {
				for (int i = 0; i < array.bytes.length; i++) {
					xoredBytes[i] ^= array.bytes[i];
				}
			}
		}
		return new ByteArray(xoredBytes);
	}

	/**
	 * Splits one ByteArray into two ByteArrays.
	 * <p>
	 * @param lengthOfFirstByteArray
	 * @return Array of ByteArrays containing two ByteArrays, namely the left and the right one.
	 */
	public ByteArray[] split(int lengthOfLeftByteArray) {
		if (lengthOfLeftByteArray < 1 || lengthOfLeftByteArray > bytes.length - 1) {
			throw new IllegalArgumentException();
		}
		ByteArray[] split = new ByteArray[]{new ByteArray(Arrays.copyOf(bytes, lengthOfLeftByteArray)), new ByteArray(Arrays.copyOfRange(bytes, lengthOfLeftByteArray, bytes.length - 1))};
		return split;
	}

	public ByteArray getHash() {
		return getHash(HashMethod.DEFAULT);
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
		for (int i = 0; i < this.getLength(); i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(this.bytes[i] & 0xFF));
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
