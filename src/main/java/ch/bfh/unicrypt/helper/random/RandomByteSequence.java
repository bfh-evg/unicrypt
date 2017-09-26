/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.random;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import java.math.BigInteger;

/**
 * The purpose of this abstract sub-class of {@link Sequence} is manyfold. First, it serves as a base implementation for
 * various types of infinitely long byte sequences. Second, it adjusts the return type of the method
 * {@link Sequence#group(int)} to {@link ByteArray}. Finally, it provides various methods for converting the random byte
 * sequence into a sequence of random {@link Boolean}, {@link Integer}, or {@link BigInteger} values.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public abstract class RandomByteSequence
	   extends Sequence<Byte> {

	protected RandomByteSequence() {
		super(Sequence.INFINITE);
	}

	@Override
	public final RandomByteArraySequence group(final int groupLength) {
		if (groupLength < 0) {
			throw new IllegalArgumentException();
		}
		final RandomByteSequence source = this;
		return new RandomByteArraySequence() {

			@Override
			public RandomByteArraySequenceIterator iterator() {
				return new RandomByteArraySequenceIterator() {

					private final RandomByteSequenceIterator iterator = source.iterator();

					@Override
					protected ByteArray abstractNext() {
						int i = 0;
						byte[] result = new byte[groupLength];
						while (i < groupLength) {
							result[i] = iterator.abstractNext();
							i++;
						}
						return SafeByteArray.getInstance(result);
					}

					@Override
					protected void updateBefore() {
						iterator.updateBefore();
					}

					@Override
					protected void updateAfter() {
						iterator.updateAfter();
					}
				};
			}

		};

	}

	/**
	 * Transforms the random byte sequence into a random bit sequence of type {@link Boolean}. The bits are generated
	 * byte-wise: negative byte values (most significant bit set to 1) lead to {@code true} and non-negative byte values
	 * (most significant bit set to 0) lead to {@code false}.
	 * <p>
	 * @return The random bit sequence
	 */
	public Sequence<Boolean> getRandomBitSequence() {
		return this.map(value -> value < 0);
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link Integer}. The resulting random
	 * values lie between {@link Integer#MIN_VALUE} and {@link Integer#MAX_VALUE} (inclusive).
	 * <p>
	 * @return The random integer sequence
	 */
	public Sequence<Integer> getRandomIntegerSequence() {
		return this.getRandomIntegerSequence(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link Integer}. The resulting random
	 * values lie between {@code 0} and {@code maxValue} (inclusive).
	 * <p>
	 * @param maxValue The upper bound of the random integers
	 * @return The random integer sequence
	 */
	public Sequence<Integer> getRandomIntegerSequence(int maxValue) {
		return this.getRandomIntegerSequence(0, maxValue);
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link Integer}. The resulting random
	 * values lie between {@code minValue} and {@code maxValue} (inclusive).
	 * <p>
	 * @param minValue The lower bound of the random integers
	 * @param maxValue The upper bound of the random integers
	 * @return The random integer sequence
	 */
	public Sequence<Integer> getRandomIntegerSequence(int minValue, int maxValue) {
		return this.getRandomBigIntegerSequence(BigInteger.valueOf(minValue), BigInteger.valueOf(maxValue)).map(value -> value.intValue());
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link BigInteger}. The bit length of
	 * the resulting random values is given. Therefore, the resulting random values lie between {@code 2^(bitLength-1)}
	 * and {@code 2^(bitLength)-1} (inclusive).
	 * <p>
	 * @param bitLength The bit length of the random integers
	 * @return The random integer sequence
	 */
	public Sequence<BigInteger> getRandomBigIntegerSequence(int bitLength) {
		if (bitLength < 0) {
			throw new IllegalArgumentException();
		}
		if (bitLength == 0) {
			this.getRandomBigIntegerSequence(MathUtil.ZERO, MathUtil.ZERO);
		}
		return this.getRandomBigIntegerSequence(MathUtil.powerOfTwo(bitLength - 1), MathUtil.powerOfTwo(bitLength).
												subtract(MathUtil.ONE));
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link BigInteger}. The resulting
	 * random values lie between {@code 0} and {@code maxValue} (inclusive).
	 * <p>
	 * @param maxValue The upper bound of the random integers
	 * @return The random integer sequence
	 */
	public Sequence<BigInteger> getRandomBigIntegerSequence(BigInteger maxValue) {
		return this.getRandomBigIntegerSequence(MathUtil.ZERO, maxValue);
	}

	/**
	 * Transforms the random byte sequence into a random integer sequence of type {@link BigInteger}. The resulting
	 * random values lie between {@code minValue} and {@code maxValue} (inclusive).
	 * <p>
	 * @param minValue The lower bound of the random integers
	 * @param maxValue The upper bound of the random integers
	 * @return The random integer sequence
	 */
	public Sequence<BigInteger> getRandomBigIntegerSequence(final BigInteger minValue, final BigInteger maxValue) {
		if (minValue == null || maxValue == null || minValue.compareTo(maxValue) > 0) {
			throw new IllegalArgumentException();
		}
		final int bitLength = maxValue.subtract(minValue).bitLength();
		return this.group(MathUtil.divideUp(bitLength, 8))
			   .map(byteArray -> {
				   if (bitLength == 0) {
					   return minValue;
				   }
				   int shift = 8 - (bitLength % 8);
				   if (shift == 8) {
					   shift = 0;
				   }
				   byte[] bytes = byteArray.getBytes();
				   bytes[0] = MathUtil.shiftRight(MathUtil.shiftLeft(bytes[0], shift), shift);
				   return new BigInteger(1, bytes).add(minValue);
			   })
			   .filter(value -> value.compareTo(maxValue) <= 0);
	}

	@Override
	public abstract RandomByteSequenceIterator iterator();

}
