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
package ch.bfh.unicrypt.random.classes;

import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class RandomNumberGenerator
	   extends UniCrypt {

	private static final long serialVersionUID = 1L;

	private final RandomByteSequence randomByteSequence;

	private RandomNumberGenerator(RandomByteSequence randomByteSequence) {
		this.randomByteSequence = randomByteSequence;
	}

	/**
	 * Generates a random boolean value.
	 * <p>
	 * @return The random boolean value
	 */
	public final boolean nextBoolean() {
		return this.randomByteSequence.getNextByte() % 2 == 1;
	}

	/**
	 * Generates a random byte between -128 and 127 (inclusive).
	 * <p>
	 * @return The random byte value
	 */
	public final byte nextByte() {
		return this.randomByteSequence.getNextByte();
	}

	/**
	 * Generates a random {@code int} value between 0 and Integer.MAX_VALUE.
	 * <p>
	 * @return The random {@code int} value
	 */
	public final int nextInt() {
		return this.nextInt(Integer.MAX_VALUE);
	}

	/**
	 * Generates a random {@code int} value between 0 and {@literal maxValue} (inclusive).
	 * <p>
	 * @param maxValue The maximal value
	 * @return The random {@code int} value
	 * @throws IllegalArgumentException if {@literal maxValue < 0}
	 */
	public final int nextInt(int maxValue) {
		if (maxValue < 0) {
			throw new IllegalArgumentException();
		}//This is a slow implementation.
		return this.nextBigInteger(BigInteger.valueOf(maxValue)).intValue();
	}

	/**
	 * Generates a random {@code int} value between {@literal minValue} (inclusive) and {@literal maxValue} (inclusive).
	 * <p>
	 * @param minValue The minimal value
	 * @param maxValue The maximal value
	 * @return The random {@code int} value
	 * @throws IllegalArgumentException if {@literal maxValue < minValue}
	 */
	public final int nextInt(int minValue, int maxValue) {
		return this.nextInt(maxValue - minValue) + minValue;
	}

	/**
	 * Generates a random {@code long} value between 0 and Long.MAX_VALUE.
	 * <p>
	 * @return The random {@code long} value
	 */
	public final long nextLong() {
		return this.nextLong(Long.MAX_VALUE);
	}

	/**
	 * Generates a random {@code long} value between 0 and {@literal maxValue} (inclusive).
	 * <p>
	 * @param maxValue The maximal value
	 * @return The random {@code long} value
	 * @throws IllegalArgumentException if {@literal maxValue < 0}
	 */
	public final long nextLong(long maxValue) {
		if (maxValue < 0) {
			throw new IllegalArgumentException();
		}//This is a slow implementation.
		return this.nextBigInteger(BigInteger.valueOf(maxValue)).longValue();
	}

	/**
	 * Generates a random {@code long} value between {@literal minValue} (inclusive) and {@literal maxValue}
	 * (inclusive).
	 * <p>
	 * @param minValue The minimal value
	 * @param maxValue The maximal value
	 * @return The random {@code long} value
	 * @throws IllegalArgumentException if {@literal maxValue < minValue}
	 */
	public final long nextLong(long minValue, long maxValue) {
		return this.nextLong(maxValue - minValue) + minValue;
	}

	/**
	 * Generates a random BigInteger value of a certain bit length.
	 * <p>
	 * @param bitLength The given bit length
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal bitLength < 0}
	 */
	public final BigInteger nextBigInteger(int bitLength) {
		if (bitLength < 0) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 0) {
			return BigInteger.ZERO;
		}
		return this.internalNextBigInteger(bitLength, true);
	}

	/**
	 * Generates a random BigInteger between 0 and {@literal maxValue} (inclusive).
	 * <p>
	 * @param maxValue The maximal value
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal maxValue} is null or if {@literal maxValue < 0}
	 */
	public final BigInteger nextBigInteger(BigInteger maxValue) {
		if (maxValue == null || maxValue.signum() < 0) {
			throw new IllegalArgumentException();
		}
		int bitLength = maxValue.bitLength();
		BigInteger randomValue;
		do {
			randomValue = this.internalNextBigInteger(bitLength, false);
		} while (randomValue.compareTo(maxValue) > 0);
		return randomValue;
	}

	/**
	 * Generates a random BigInteger value between {@literal minValue} (inclusive) and {@literal maxValue} (inclusive).
	 * <p>
	 * @param minValue The minimal value
	 * @param maxValue The maximal value
	 * @return The random BigInteger value
	 * @throws IllegalArgumentException if {@literal minValue} or {@literal maxValue} is null, or if
	 *                                  {@literal maxValue < minValue}
	 */
	public final BigInteger nextBigInteger(BigInteger minValue, BigInteger maxValue) {
		if (minValue == null || maxValue == null) {
			throw new IllegalArgumentException();
		}
		return this.nextBigInteger(maxValue.subtract(minValue)).add(minValue);
	}

	private BigInteger internalNextBigInteger(int bitLength, boolean isMsbSet) {
		if (bitLength < 1) {
			return BigInteger.ZERO;
		}
		int amountOfBytes = MathUtil.divideUp(bitLength, 8);
		byte[] bytes = this.randomByteSequence.getNextByteArray(amountOfBytes).getBytes();

		int shift = 8 - (bitLength % 8);
		if (shift == 8) {
			shift = 0;
		}
		if (isMsbSet) {
			bytes[0] = MathUtil.setBit(bytes[0], 7);
		}
		bytes[0] = MathUtil.shiftRight(bytes[0], shift);

		return new BigInteger(1, bytes);
	}

	public static RandomNumberGenerator getInstance() {
		return RandomNumberGenerator.getInstance(HybridRandomByteSequence.getInstance());
	}

	public static RandomNumberGenerator getInstance(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		return new RandomNumberGenerator(randomByteSequence);
	}

}
