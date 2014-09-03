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
import java.util.Iterator;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class BitArray
	   extends AbstractArray<BitArray>
	   implements Iterable<Boolean> {

	ByteArray byteArray;
	private int trailer; // number of trailing zeros not included in byteArray
	private int header; // number of leading zeros not included in byteArray

	private BitArray(ByteArray byteArray, int length) {
		this(byteArray, 0, length, 0, 0, false);
	}

	private BitArray(ByteArray byteArray, int offset, int length, int trailer, int header, boolean reverse) {
		super(length, offset, reverse);
		this.byteArray = byteArray;
		this.trailer = trailer;
		this.header = header;
	}

	public boolean[] getAll() {
		boolean[] result = new boolean[this.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	// filled up with leading zeros
	public byte[] getAllBytes() {
		int byteLength = (this.length + Byte.SIZE - 1) / Byte.SIZE;
		byte[] result = new byte[byteLength];
		for (int i = 0; i < byteLength; i++) {
			result[i] = this.getByteAt(i);
		}
		return result;
	}

	public boolean getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		return this.abstractGetAt(index);
	}

	public byte getByteAt(int byteIndex) {
		byte result = 0;
		int bitIndex = byteIndex * Byte.SIZE;
		int maxBitIndex = Math.min(this.length, bitIndex + Byte.SIZE);
		for (int i = 0; bitIndex < maxBitIndex; i++, bitIndex++) {
			if (this.abstractGetAt(bitIndex)) {
				result = ByteArray.setBit(result, i);
			}
		}
		return result;
	}

	// leading here means the highest indices
	public int countLeadingZeros() {
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.abstractGetAt(i)) {
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
			if (this.abstractGetAt(i)) {
				return result;
			}
			result++;
		}
		return result;
	}

	public BitArray stripLeadingZeros() {
		int n = this.countLeadingZeros();
		return this.stripSuffix(n);
	}

	public BitArray stripTrailingZeros() {
		int n = this.countTrailingZeros();
		return this.stripPrefix(n);
	}

	public BitArray reverse() {
		return new BitArray(this.byteArray, this.offset, this.length, this.trailer, this.header, !this.reverse);
	}

	public int countOnes() {
		return this.length - this.countZeros();
	}

	public int countZeros() {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (!this.getAt(i)) {
				result++;
			}
		}
		return result;
	}

	// left here means making the bit array smaller
	public BitArray shiftLeft(int n) {
		if (n < 0) {
			return this.shiftRight(-n);
		}
		return this.stripPrefix(Math.min(this.length, n));
	}

	// right here means making the bit array larger
	public BitArray shiftRight(int n) {
		if (n <= 0) {
			return this.shiftLeft(-n);
		}
		if (this.reverse) {
			return new BitArray(this.byteArray, this.offset, this.length + n, this.trailer, this.header + n, this.reverse);
		} else {
			return new BitArray(this.byteArray, this.offset, this.length + n, this.trailer + n, this.header, this.reverse);
		}
	}

	public ByteArray getHashValue() {
		return this.getHashValue(HashAlgorithm.getInstance());
	}

	public ByteArray getHashValue(HashAlgorithm hashAlgorithm) {
		if (hashAlgorithm == null) {
			throw new IllegalArgumentException();
		}
		byte[] hash = hashAlgorithm.getHashValue(this.getAllBytes());
		return new ByteArray(hash);
	}

	public static BitArray getInstance() {
		return BitArray.getInstance(new boolean[0]);
	}

	public static BitArray getInstance(int length) {
		return BitArray.getInstance(length, false);
	}

	public static BitArray getInstance(int length, boolean bit) {
		if (length < 0) {
			throw new IllegalArgumentException();
		}
		if (bit) {
			int byteLength = (length + Byte.SIZE - 1) / Byte.SIZE;
			return new BitArray(ByteArray.getInstance(byteLength, true), length);
		}
		return new BitArray(ByteArray.getInstance(), 0, length, length, 0, false);
	}

	public static BitArray getInstance(boolean... bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		return new BitArray(bitsToByteArray(bits), bits.length);
	}

	public static BitArray getInstance(byte... bytes) {
		return BitArray.getInstance(ByteArray.getInstance(bytes));
	}

	public static BitArray getInstance(byte[] bytes, int length) {
		return BitArray.getInstance(ByteArray.getInstance(bytes), length);
	}

	public static BitArray getInstance(ByteArray byteArray) {
		if (byteArray == null) {
			throw new IllegalArgumentException();
		}
		return new BitArray(byteArray, byteArray.getLength() * Byte.SIZE);
	}

	public static BitArray getInstance(ByteArray byteArray, int length) {
		if (byteArray == null || length > byteArray.getLength() * Byte.SIZE) {
			throw new IllegalArgumentException();
		}
		return new BitArray(byteArray, length);
	}

	// a string of '0's and '1's
	public static BitArray getInstance(String string) {
		if (string == null) {
			throw new IllegalArgumentException();
		}
		boolean[] bits = new boolean[string.length()];
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (c == '0') {
				bits[i] = false;
			} else if (c == '1') {
				bits[i] = true;
			} else {
				throw new IllegalArgumentException();
			}
		}
		return BitArray.getInstance(bits);
	}

	public static BitArray getRandomInstance(int length) {
		return BitArray.getRandomInstance(length, HybridRandomByteSequence.getInstance());
	}

	public static BitArray getRandomInstance(int length, RandomByteSequence randomByteSequence) {
		if (length < 0 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		int byteLength = (length + Byte.SIZE - 1) / Byte.SIZE;
		return new BitArray(randomByteSequence.getNextByteArray(byteLength), length);
	}

	@Override
	protected String defaultToStringName() {
		return "";
	}

	@Override
	protected String defaultToStringValue() {
		String str = "";
		for (int i = 0; i < this.length; i++) {
			if (i > 0 && i % 8 == 0) {
				str = str + "|";
			}
			if (this.getAt(i)) {
				str = str + "1";
			} else {
				str = str + "0";
			}
		}
		return "\"" + str + "\"";
	}

	@Override
	public Iterator<Boolean> iterator() {
		return new Iterator<Boolean>() {

			int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return currentIndex < length;
			}

			@Override
			public Boolean next() {
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
		for (boolean b : this) {
			hash = 79 * hash + ((b) ? 1 : 0);
		}
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BitArray)) {
			return false;
		}
		final BitArray other = (BitArray) obj;
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

	private boolean abstractGetAt(int index) {
		if (this.reverse) {
			index = this.length - index - 1;
		}
		if (index < this.trailer || index >= this.length - this.header) {
			return false;
		}
		return this.byteArray.getBitAt(this.offset + index - this.trailer);
	}

	@Override
	protected BitArray abstractExtract(int offset, int length) {
		int newTrailer = Math.max(0, this.trailer - offset);
		int newOffset = this.offset + Math.max(0, offset - this.trailer);
		return new BitArray(this.byteArray, newOffset, length, newTrailer, this.header, this.reverse);
	}

	@Override
	protected BitArray abstractAppend(BitArray other) {
		boolean[] result = new boolean[this.length + other.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.getAt(i);
		}
		for (int i = 0; i < other.length; i++) {
			result[this.length + i] = other.getAt(i);
		}
		return new BitArray(bitsToByteArray(result), result.length);
	}

	private static ByteArray bitsToByteArray(boolean[] bits) {
		int byteLength = (bits.length + Byte.SIZE - 1) / Byte.SIZE;
		byte[] bytes = new byte[byteLength];
		for (int i = 0; i < bits.length; i++) {
			int byteIndex = i / Byte.SIZE;
			int bitIndex = i % Byte.SIZE;
			if (bits[i]) {
				bytes[byteIndex] = ByteArray.setBit(bytes[byteIndex], bitIndex);
			}
		}
		return ByteArray.getInstance(bytes);
	}

	@Override
	protected Class<BitArray> getArrayClass() {
		return BitArray.class;
	}

}
