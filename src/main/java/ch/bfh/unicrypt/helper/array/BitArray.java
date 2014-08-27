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

import ch.bfh.unicrypt.random.classes.HybridRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class BitArray
	   extends AbstractImmutableArray<BitArray> {

	ByteArray byteArray;
	int offset;
	int zeros; // number of leading zeros

	private BitArray(ByteArray byteArray, int length) {
		this(byteArray, 0, length, 0);
	}

	private BitArray(ByteArray byteArray, int offset, int length, int zeros) {
		super(length);
		this.offset = offset;
		this.zeros = zeros;
		this.byteArray = byteArray;
	}

	public boolean[] getAll() {
		boolean[] result = new boolean[this.length];
		for (int i = 0; i < this.length; i++) {
			result[i] = this.abstractGetAt(i);
		}
		return result;
	}

	public boolean getAt(int index) {
		if (index < 0 || index >= this.length) {
			throw new IndexOutOfBoundsException();
		}
		if (index < this.zeros) {
			return false;
		}
		return this.abstractGetAt(index);
	}

	private boolean abstractGetAt(int index) {
		return this.byteArray.getBitAt(this.offset + index - this.zeros);
	}

	// leading here means the highest indices
	public int countLeadingZeros() {
		int result = 0;
		for (int i = this.length - 1; i >= 0; i--) {
			if (this.getAt(i)) {
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
			if (this.getAt(i)) {
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

	public int countOnes() {
		int result = 0;
		for (int i = 0; i < this.length; i++) {
			if (this.getAt(i)) {
				result++;
			}
		}
		return result;
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
		if (n == 0) {
			return this;
		}
		if (n < 0) {
			return this.shiftRight(-n);
		}
		n = Math.min(n, this.length);
		return this.stripPrefix(n);
	}

	// right here means making the bit array larger
	public BitArray shiftRight(int n) {
		if (n == 0) {
			return this;
		}
		if (n < 0) {
			return this.shiftLeft(-n);
		}
		return new BitArray(this.byteArray, this.offset, this.length + n, this.zeros + n);
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
			return new BitArray(ByteArray.getInstance(byteLength, true), 0, length, 0);
		}
		return new BitArray(ByteArray.getInstance(), 0, length, length);
	}

	public static BitArray getInstance(boolean... bits) {
		if (bits == null) {
			throw new IllegalArgumentException();
		}
		return new BitArray(bitsToByteArray(bits), bits.length);
	}

	public static BitArray getInstance(byte... bytes) {
		if (bytes == null) {
			throw new IllegalArgumentException();
		}
		return BitArray.getInstance(bytes, bytes.length * Byte.SIZE);
	}

	public static BitArray getInstance(byte[] bytes, int length) {
		return new BitArray(ByteArray.getInstance(bytes), length);
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
	protected BitArray abstractExtract(int offset, int length) {
		int newZeros = Math.max(0, this.zeros - offset);
		int newOffset = this.offset + Math.max(0, offset - this.zeros);
		return new BitArray(this.byteArray, newOffset, length, newZeros);
	}

	@Override
	protected BitArray abstractConcatenate(BitArray other) {
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
				bytes[byteIndex] = (byte) (bytes[byteIndex] | (1 << bitIndex));
			}
		}
		return ByteArray.getInstance(bytes);
	}

	@Override
	protected Class<BitArray> getBaseClass() {
		return BitArray.class;
	}

}
